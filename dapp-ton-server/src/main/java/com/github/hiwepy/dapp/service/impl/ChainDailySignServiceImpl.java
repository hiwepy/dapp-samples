package com.github.hiwepy.dapp.service.impl;

import com.github.hiwepy.dapp.entity.User;
import com.github.hiwepy.dapp.exception.BizExceptionCode;
import com.github.hiwepy.dapp.mapper.ChainDailySignLogMapper;
import com.github.hiwepy.dapp.mapper.ChainDailySignUserMapper;
import com.github.hiwepy.dapp.mapper.UserMapper;
import com.github.hiwepy.dapp.param.ChainDailySignClaimReq;
import com.github.hiwepy.dapp.service.ChainDailySignService;
import com.github.hiwepy.dapp.service.TonChainService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RedissonClient;
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

        tonChainService.getTx(String address)

        // 查询用户交易信息
        RdDwsUserTrade dwsUserTrade = rdDwsUserTradeMapper.selectByUserId(req.getUserId(), TradeTypeEnum.ANALOG.name());
        if (Objects.isNull(dwsUserTrade)) {
            log.info("ActivityServiceImpl.dailySignClaim dwsUserTrade is null");
            return Result.failure(ErrorCode.CLAIM_NOT_SATISFY);
        }
        UserDailySignDTO userDailySignInfo = getUserDailySignInfo(user.getId(), dwsUserTrade);
        if (!UserDailySignStatusEnum.WAIT_CLAIM.name().equalsIgnoreCase(userDailySignInfo.getClaimStatus())) {
            log.info("ActivityServiceImpl.dailySignClaim getUserDailySignInfo,resp:{}", JSONUtils.obj2json(userDailySignInfo));
            return Result.failure(ErrorCode.CLAIM_NOT_SATISFY);
        }

        // 查询用户的链上签到汇总信息
        ChainDailySignUser chainDailySignUser = chainDailySignUserMapper.selectByUserId(req.getUserId());
        // 查询链上每日签到配置信息
        List<ConfigChainDailySign> chainDailySignConfigInfoList = commonScheduleService.getChainDailySignInfos();
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
                return Result.success(Map.of("claimStatus", UserDailySignStatusEnum.CLAIMED.name()));
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
            return Result.failure(ErrorCode.CLAIM_FAILED);
        }
        // 如果 chainDailySignUserSave 对象不为空，插入用户签到信息
        if (Objects.nonNull(chainDailySignUserSave)) {
            chainDailySignUserMapper.insertSelective(chainDailySignUserSave);
        }
        // 如果 chainDailySignUserUpdate 对象不为空，更新用户签到信息
        if (Objects.nonNull(chainDailySignUserUpdate)) {
            chainDailySignUserMapper.updateByPrimaryKeySelective(chainDailySignUserUpdate);
        }
        // 生成一条链上签到记录
        ChainDailySignLog chainDailySignLog = ChainDailySignLog.buildChainDailySignLog(req.getUserId(), appId, req.getAddress(), req.getHashAddress());
        chainDailySignLogMapper.insertSelective(chainDailySignLog);
        // 从redis中获取用户资产信息
        String innerDecKey = OptionsConstants.Redis.USER_ASSETS_TOKEN_CONE;
        RMap<Long, String> map = redissonClient.getMap(innerDecKey, StringCodec.INSTANCE);
        String userAvailableBalance = map.get(req.getUserId());
        userAvailableBalance = StringUtils.isBlank(userAvailableBalance) ? "0" : userAvailableBalance;
        // 计算用户签到后的资产
        BigDecimal newAmount = new BigDecimal(userAvailableBalance).add(dailySign.getReward());
        map.put(user.getId(), newAmount.stripTrailingZeros().toPlainString());
        return Result.success(Map.of("claimStatus", UserDailySignStatusEnum.CLAIMED.name()));
    }



}
