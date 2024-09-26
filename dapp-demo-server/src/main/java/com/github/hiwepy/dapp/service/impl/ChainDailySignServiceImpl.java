package com.github.hiwepy.dapp.service.impl;

import com.github.hiwepy.api.util.DateUtils;
import com.github.hiwepy.api.util.TimezoneEnum;
import com.github.hiwepy.dapp.constant.BizRedisKey;
import com.github.hiwepy.dapp.entity.ChainDailySignLog;
import com.github.hiwepy.dapp.entity.ChainDailySignUser;
import com.github.hiwepy.dapp.entity.ConfigChainDailySign;
import com.github.hiwepy.dapp.entity.User;
import com.github.hiwepy.dapp.enums.UserDailySignStatusEnum;
import com.github.hiwepy.dapp.exception.BizExceptionCode;
import com.github.hiwepy.dapp.mapper.ChainDailySignLogMapper;
import com.github.hiwepy.dapp.mapper.ChainDailySignUserMapper;
import com.github.hiwepy.dapp.mapper.UserMapper;
import com.github.hiwepy.dapp.param.ChainDailySignClaimReq;
import com.github.hiwepy.dapp.service.ChainDailySignService;
import com.github.hiwepy.dapp.service.ConfigChainDailySignService;
import com.github.hiwepy.dapp.service.TonChainService;
import com.github.hiwepy.dapp.util.TonTransactionUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
public class ChainDailySignServiceImpl implements ChainDailySignService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private ConfigChainDailySignService configChainDailySignService;
    @Resource
    private ChainDailySignUserMapper chainDailySignUserMapper;
    @Resource
    private ChainDailySignLogMapper chainDailySignLogMapper;
    @Resource
    private RedissonClient redissonClient;
    @Resource
    private TonChainService tonChainService;

    @Override
    public Map<String, String> dailySignClaim(String appId, ChainDailySignClaimReq req) {
        // 1、检查用户是否存在
        User user = userMapper.selectById(req.getUserId());
        if (Objects.isNull(user)) {
            log.info("ChainDailySignServiceImpl.dailySignClaim userId:{}", req.getUserId());
            BizExceptionCode.USER_NOT_FOUND.throwException();
        }
        // 检查交易是否由指定地址发起
        boolean checked = TonTransactionUtil.checkTransaction(req.getHashAddress(), req.getAddress());
        if (!checked) {
            BizExceptionCode.SIGN_CLAIM_FAILED.throwException();
        }
        // 查询用户的链上签到汇总信息
        ChainDailySignUser chainDailySignUser = chainDailySignUserMapper.selectById(req.getUserId());
        // 查询链上每日签到配置信息
        List<ConfigChainDailySign> chainDailySignConfigInfoList = configChainDailySignService.getChainDailySignInfos();
        // 初始化变量
        Integer durationDays = 1;
        ChainDailySignUser chainDailySignUserSave = null;
        ChainDailySignUser chainDailySignUserUpdate = null;
        // 如果用户没有签到记录，说明是第一次签到
        if (Objects.isNull(chainDailySignUser)) {
            // 初始化用户签到信息
            chainDailySignUserSave = ChainDailySignUser.buildChainDailySignUserSave(req.getUserId(), durationDays);
        } else {
            // UTC时区
            TimezoneEnum utcTimezoneEnum = TimezoneEnum.UTC;
            // 获取用户最近一次签到时间所在日期的开始时间
            long lastSignInDay = DateUtils.utcConvertUnitDayByZoneId(chainDailySignUser.getLastSignInAt(), utcTimezoneEnum);
            // 获取今日开始时间
            long thisDayStartAt = DateUtils.utcStartByZoneId(utcTimezoneEnum);
            // 如果用户今日已经签到，返回已签到
            if (lastSignInDay >= thisDayStartAt) {
                return Map.of("claimStatus", UserDailySignStatusEnum.CLAIMED.name());
            }
            // lastSignInDay 与 thisDayStartAt 相差一天，说明用户连续签到
            if (lastSignInDay + 86_400_000 == thisDayStartAt) {
                durationDays = chainDailySignUser.getDurationDays() + 1;
            }
            // 持续天数大于配置的签到天数，重置为1
            if (durationDays > chainDailySignConfigInfoList.size()) {
                durationDays = 1;
            }
            // 更新用户签到信息
            chainDailySignUserUpdate = ChainDailySignUser.buildChainDailySignUserUpdate(chainDailySignUser, durationDays);
        }
        // 根据持续天数获对应的签到奖励配置信息
        int finalDurationDays = durationDays;
        ConfigChainDailySign dailySign = chainDailySignConfigInfoList.stream()
                .filter(p -> p.getDay() != null && p.getDay() == finalDurationDays)
                .findFirst()
                .orElse(null);
        // 如果没有找到对应的签到奖励配置信息，返回签到失败
        if (Objects.isNull(dailySign)) {
            BizExceptionCode.SIGN_CLAIM_FAILED.throwException();
        }
        // 如果 chainDailySignUserSave 对象不为空，插入用户签到信息
        if (Objects.nonNull(chainDailySignUserSave)) {
            chainDailySignUserMapper.insert(chainDailySignUserSave);
        }
        // 如果 chainDailySignUserUpdate 对象不为空，更新用户签到信息
        if (Objects.nonNull(chainDailySignUserUpdate)) {
            chainDailySignUserMapper.updateById(chainDailySignUserUpdate);
        }
        // 生成一条链上签到记录
        ChainDailySignLog chainDailySignLog = ChainDailySignLog.buildChainDailySignLog(req.getUserId(), appId, req.getAddress(), req.getHashAddress());
        chainDailySignLogMapper.insert(chainDailySignLog);
        // 从redis中获取用户资产信息
        String innerDecKey =  BizRedisKey.USER_ASSETS_TOKEN.getKey();
        RMap<Long, String> map = redissonClient.getMap(innerDecKey, StringCodec.INSTANCE);
        String userAvailableBalance = map.get(req.getUserId());
        userAvailableBalance = StringUtils.isBlank(userAvailableBalance) ? "0" : userAvailableBalance;
        // 计算用户签到后的资产
        BigDecimal newAmount = new BigDecimal(userAvailableBalance).add(dailySign.getReward());
        map.put(user.getId(), newAmount.stripTrailingZeros().toPlainString());
        return Map.of("claimStatus", UserDailySignStatusEnum.CLAIMED.name());
    }

}
