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
import com.github.hiwepy.dapp.util.Ton4jSdkUtil;
import com.github.hiwepy.dapp.util.TonHttpApiUtil;
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
        // 2、根据UserId查询链上每日用户签到表
        ChainDailySignUser chainDailySignUser = chainDailySignUserMapper.selectById(req.getUserId());
        // 3、查询链上每日签到奖励配置表
        List<ConfigChainDailySign> chainDailySignConfigInfoList = configChainDailySignService.getChainDailySignInfos();
        // 连续签到天数
        Integer durationDays = 1;
        // 累计签到天数
        Integer seriesDays = 1;
        ChainDailySignUser chainDailySignUserSave = null;
        ChainDailySignUser chainDailySignUserUpdate = null;
        // 使用 UTC时区
        TimezoneEnum utcTimezoneEnum = TimezoneEnum.UTC;
        // 获取今日开始时间
        long thisDayStartAt = DateUtils.utcStartByZoneId(utcTimezoneEnum);
        // 4、如果用户没有签到记录，说明是第一次签到
        if (Objects.isNull(chainDailySignUser)) {
            // 4.1、通过TON HTTP API 检查 Ton 指定钱包地址今日是否有交易
            boolean checked = TonHttpApiUtil.checkByTonHttpApi(req.getAddress(), thisDayStartAt);
            if (!checked) {
                BizExceptionCode.SIGN_CLAIM_FAILED.throwException();
            }
            // 4.2、如果今日有交易，创建用户第一次签到信息对象
            chainDailySignUserSave = ChainDailySignUser.buildChainDailySignUserSave(req.getUserId(), seriesDays, durationDays);
        }
        // 5、如果用户有签到记录，说明不是第一次签到
        else {
            // 获取用户最近一次签到时间所在日期的开始时间
            long lastSignInDay = DateUtils.utcConvertUnitDayByZoneId(chainDailySignUser.getLastSignInAt(), utcTimezoneEnum);
            // 5.1、如果数据表中的最后一次签到时间大于等于今日开始时间，说明用户今日已经签到，返回签到成功，无需进行后续逻辑
            if (lastSignInDay >= thisDayStartAt) {
                return Map.of("claimStatus", UserDailySignStatusEnum.CLAIMED.name());
            }
            // 5.2、如果今日没有签到，通过TON HTTP API 检查 Ton 指定钱包地址今日是否有交易
            boolean checked = TonHttpApiUtil.checkByTonHttpApi(req.getAddress(), thisDayStartAt);
            if (!checked) {
                BizExceptionCode.SIGN_CLAIM_FAILED.throwException();
            }
            // 5.3、如果数据表中的最后一次签到时间 + 86_400_000 秒 等于今日开始时间，说明用户昨日已经签到，今日签到计入连续签到
            if (lastSignInDay + 86_400_000 == thisDayStartAt) {
                durationDays = chainDailySignUser.getDurationDays() + 1;
                seriesDays = chainDailySignUser.getSeriesDays() + 1;
            }
            // 5.4、如果持续天数大于配置的签到天数，重置为1
            if (durationDays > chainDailySignConfigInfoList.size()) {
                durationDays = 1;
            }
            // 5.5、更新用户签到信息对象
            chainDailySignUserUpdate = ChainDailySignUser.buildChainDailySignUserUpdate(chainDailySignUser, seriesDays, durationDays);
        }
        // 6、根据持续天数获对应的签到奖励配置信息
        int finalDurationDays = durationDays;
        ConfigChainDailySign dailySign = chainDailySignConfigInfoList.stream()
                .filter(p -> p.getDay() != null && p.getDay() == finalDurationDays)
                .findFirst()
                .orElse(null);
        // 6.1、如果没有找到对应的签到奖励配置信息，返回签到失败
        if (Objects.isNull(dailySign)) {
            BizExceptionCode.SIGN_CLAIM_FAILED.throwException();
        }
        // 7、保存或更新用户签到信息
        // 7.1、如果 chainDailySignUserSave 对象不为空，说明是第一次签到，插入用户签到信息
        if (Objects.nonNull(chainDailySignUserSave)) {
            chainDailySignUserMapper.insert(chainDailySignUserSave);
        }
        // 7.2、如果 chainDailySignUserUpdate 对象不为空，说明不是第一次签到，更新用户签到信息
        if (Objects.nonNull(chainDailySignUserUpdate)) {
            chainDailySignUserMapper.updateById(chainDailySignUserUpdate);
        }
        // 8、生成一条链上签到记录
        ChainDailySignLog chainDailySignLog = ChainDailySignLog.buildChainDailySignLog(req.getUserId(), appId, req.getAddress(), req.getHashAddress());
        chainDailySignLogMapper.insert(chainDailySignLog);
        // 9、从redis中获取用户资产信息
        String innerDecKey = BizRedisKey.USER_ASSETS_TOKEN.getKey();
        RMap<Long, String> map = redissonClient.getMap(innerDecKey, StringCodec.INSTANCE);
        String userAvailableBalance = map.get(req.getUserId());
        userAvailableBalance = StringUtils.isBlank(userAvailableBalance) ? "0" : userAvailableBalance;
        // 9.1、计算并更新用户签到后的资产
        BigDecimal newAmount = new BigDecimal(userAvailableBalance).add(dailySign.getReward());
        map.put(user.getId(), newAmount.stripTrailingZeros().toPlainString());
        // 10、返回签到成功
        return Map.of("claimStatus", UserDailySignStatusEnum.CLAIMED.name());
    }

}
