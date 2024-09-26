package com.github.hiwepy.dapp.constant;

import com.github.hiwepy.api.constant.RedisKey;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.function.BiFunction;

public enum BizRedisKey {

    USER_LOCK_REG("用户注册锁", (account, p2) -> {
        return RedisKey.getKeyStr(BizRedisKeyConstant.USER_LOCK_REG_KEY, account);
    }),
    /**
     * 用户任务列表
     */
    USER_TASK_DAY("每日任务列表", (userId, p2) -> {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(BizRedisKeyConstant.YYYYMMDD);
        return RedisKey.getKeyStr(BizRedisKeyConstant.USER_TASK_KEY, userId, LocalDate.now().format(formatter));
    }),
    /**
     * redis 用户登陆信息
     */
    USER_LOGIN_INFO("用户登陆信息", (userId, p2) -> {
        return RedisKey.getKeyStr(BizRedisKeyConstant.USER_LOGIN_INFO_KEY, userId);
    }),
    USER_LOGIN_IDENTITY_LIST("用户登陆身份信息", (userId, p2) -> {
        return RedisKey.getKeyStr(BizRedisKeyConstant.USER_LOGIN_IDENTITY_LIST_KEY, userId);
    }),
    USER_LOGIN_SUB_ACCOUNT_LIST("用户登陆关联子账号信息", (userId, p2) -> {
        return RedisKey.getKeyStr(BizRedisKeyConstant.USER_LOGIN_SUB_ACCOUNT_LIST_KEY, userId);
    }),
    /**
     * 用于记录用户登录后的信息变化状态
     * 如：用户信息、用户权限、用户角色、用户资产等
     */
    USER_LOGIN_STATUS("用户登陆状态信息", (userId, p2) -> {
        return RedisKey.getKeyStr(BizRedisKeyConstant.USER_LOGIN_STATUS_KEY, userId);
    }),
    /**
     * redis 用户登陆次数
     */
    USER_LOGIN_AMOUNT("用户登陆次数", (userId, p2) -> {
        return RedisKey.getKeyStr(BizRedisKeyConstant.USER_LOGIN_AMOUNT_KEY, userId);
    }),
    /**
     * redis 用户 token
     */
    USER_TOKEN("用户 token", (userId, p2) -> {
        return RedisKey.getKeyStr(BizRedisKeyConstant.USER_TOKEN_KEY, userId);
    }),
    /**
     * User Token 的 rsa 私钥缓存
     */
    USER_TOKEN_PRI_KEY("User Token 的 rsa 私钥缓存", (p1, p2)->{
        return RedisKey.getKeyStr(BizRedisKeyConstant.USER_TOKEN_PRI_KEY);
    }),
    /**
     * User Token 的 rsa 公钥缓存
     */
    USER_TOKEN_PUB_KEY(" User Token 的 rsa 公钥缓存", (p1, p2)->{
        return RedisKey.getKeyStr(BizRedisKeyConstant.USER_TOKEN_PUB_KEY);
    }),

    /**
     * redis 用户黑名单缓存
     */
    USER_BLACKLIST("用户黑名单", (userId, p2) -> {
        return RedisKey.getKeyStr(BizRedisKeyConstant.USER_BLACKLIST_KEY, userId);
    }),
    /**
     * redis 我关注的用户
     */
    USER_FOLLOW("我关注的用户", (userId, p2) -> {
        return RedisKey.getKeyStr(BizRedisKeyConstant.USER_FOLLOW_KEY, userId);
    }),
    /**
     * redis 关注我的用户
     */
    USER_FOLLOWERS("关注我的用户", (userId, p2) -> {
        return RedisKey.getKeyStr(BizRedisKeyConstant.USER_FOLLOWERS_KEY, userId);
    }),
    /**
     * redis 新关注我的用户
     */
    USER_NEW_FOLLOWERS("新关注我的用户", (userId, p2) -> {
        return RedisKey.getKeyStr(BizRedisKeyConstant.USER_NEW_FOLLOWERS_KEY, userId);
    }),
    /**
     * redis 用户账号缓存
     */
    USER_ACCOUNT("用户账号缓存", (type, account) -> {
        return RedisKey.getKeyStr(BizRedisKeyConstant.USER_ACCOUNT_KEY, type, account);
    }),
    /**
     * redis 用户code缓存
     */
    USER_CODE("用户code缓存", (userId, p2) -> {
        return RedisKey.getKeyStr(BizRedisKeyConstant.USER_CODE_KEY, userId);
    }),
    /**
     * redis 用户信息
     */
    USER_INFO("用户信息", (userId, p2) -> {
        return RedisKey.getKeyStr(BizRedisKeyConstant.USER_INFO_KEY, userId);
    }),
    /**
     * redis 用户角色
     */
    USER_ROLE("用户角色", (userId, p2) -> {
        return RedisKey.getKeyStr(BizRedisKeyConstant.USER_ROLE_KEY, userId);
    }),
    /**
     * redis 用户权限
     */
    USER_PERMS("用户权限", (userId, p2) -> {
        //String keyStr = MessageFormatter.format(BizRedisKeyConstant.USER_PERMS_KEY, userId).getMessage();
        return RedisKey.getKeyStr(BizRedisKeyConstant.USER_PERMS_KEY, userId);
    }),
    /**
     * redis 用户单点登录状态
     */
    USER_SSO_STATE("用户单点登录状态", (userId, p2) -> {
        return RedisKey.getKeyStr(BizRedisKeyConstant.USER_SSO_STATE_KEY, userId);
    }),
    /**
     * 用户会话列表
     */
    USER_SESSIONS("用户会话列表", (p1, p2) -> {
        return RedisKey.getKeyStr(BizRedisKeyConstant.USER_SESSIONS_KEY);
    }),
    /**
     * 用户会话信息
     */
    USER_SESSION("用户会话信息", (sessionId, p2) -> {
        return RedisKey.getKeyStr(BizRedisKeyConstant.USER_SESSION_KEY, sessionId);
    }),
    /**
     * 用户坐标缓存
     */
    USER_GEO_LOCATION("用户坐标", (userId, p2) -> {
        return RedisKey.getKeyStr(BizRedisKeyConstant.USER_GEO_LOCATION_KEY);
    }),
    /**
     * 用户坐标对应的地理位置缓存
     */
    USER_GEO_INFO("用户坐标对应的地理位置缓存", (userId, p2) -> {
        return RedisKey.getKeyStr(BizRedisKeyConstant.USER_GEO_INFO_KEY);
    }),
    /**
     * 用户资产缓存
     */
     USER_ASSETS_AMOUNT("用户资产", (userId, p2)->{
         String keyStr = MessageFormatter.format(BizRedisKeyConstant.USER_ASSETS_AMOUNT_KEY, userId).getMessage();
         return RedisKey.getKeyStr(keyStr);
     }),
    /**
     * 用户资产缓存
     */
    USER_ASSETS_TOKEN("用户资产", (p1, p2)->{
        return RedisKey.getKeyStr(BizRedisKeyConstant.USER_EXCHANGE_AMOUNT);
    }),
    /**
     * 用户资产换算临时缓存
     */
    USER_EXCHANGE_AMOUNT("用户资产换算临时缓存", (userId, p2) -> {
        return RedisKey.getKeyStr(BizRedisKeyConstant.USER_EXCHANGE_AMOUNT, userId);
    }),
    /**
     * 用户金币增量缓存
     */
    USER_COIN_AMOUNT("用户金币", (userId, p2) -> {
        return RedisKey.getKeyStr(BizRedisKeyConstant.USER_COIN_AMOUNT_KEY, userId);
    }),
    /**
     * 用户珍珠增量缓存
     */
    USER_PEARL_AMOUNT("用户珍珠", (userId, p2) -> {
        return RedisKey.getKeyStr(BizRedisKeyConstant.USER_PEARL_AMOUNT_KEY, userId);
    }),
    /**
     * 用户经验增量缓存
     */
    USER_EXP_AMOUNT("用户经验", (userId, p2) -> {
        return RedisKey.getKeyStr(BizRedisKeyConstant.USER_EXP_AMOUNT_KEY, userId);
    }),
    /**
     * 用户权益缓存
     */
    USER_RITHTS("用户权益", (userId, p2) -> {
        return RedisKey.getKeyStr(BizRedisKeyConstant.USER_RITHTS_KEY, userId);
    }),
    /**
     * 消息队列消息消费锁
     */
    MQ_CONSUME_LOCK("消息队列消息消费锁", (msgKey, p2) -> {
        return RedisKey.getKeyStr(BizRedisKeyConstant.MQ_CONSUME_LOCK, Objects.toString(msgKey));
    }),
    /**
     * IP地区编码缓存
     */
    IP_REGION_INFO("用户坐标对应的地区编码缓存", (ip, p2) -> {
        return RedisKey.getKeyStr(BizRedisKeyConstant.IP_REGION_KEY, ip);
    }),
    /**
     * IP坐标缓存
     */
    IP_LOCATION_INFO("用户坐标对应的地理位置缓存", (ip, p2) -> {
        return RedisKey.getKeyStr(BizRedisKeyConstant.IP_LOCATION_KEY, ip);
    }),
    /**
     * IP坐标缓存（百度服务缓存）
     */
    IP_LOCATION_BAIDU_INFO("IP坐标缓存（百度服务缓存）", (ip, p2) -> {
        return RedisKey.getKeyStr(BizRedisKeyConstant.IP_BAIDU_LOCATION_KEY, ip);
    }),
    /**
     * IP坐标缓存（太平洋网络）
     */
    IP_LOCATION_PCONLINE_INFO("IP坐标缓存（太平洋网络）", (ip, p2) -> {
        return RedisKey.getKeyStr(BizRedisKeyConstant.IP_PCONLINE_LOCATION_KEY, ip);
    }),
    /**
     * 接口幂等缓存（Token模式）
     */
    IDEMPOTENT_TOKEN_KEY("接口幂等缓存（Token模式）", (token, p2) -> {
        return RedisKey.getKeyStr(BizRedisKeyConstant.IDEMPOTENT_TOKEN_KEY, token);
    }),

    /**
     * 接口幂等缓存（参数模式）
     */
    IDEMPOTENT_ARGS_KEY("接口幂等缓存（参数模式）", (args, p2) -> {
        return RedisKey.getKeyStr(BizRedisKeyConstant.IDEMPOTENT_ARGS_KEY, args);
    }),
    /**
     * redis 应用黑名单缓存
     */
    APP_BLACKLIST("应用黑名单", (appId, type) -> {
        return RedisKey.getKeyStr(BizRedisKeyConstant.APP_BLACKLIST_KEY, appId);
    }),
    /**
     * 数据字典缓存
     */
    APP_DICT("数据字典缓存", (appId, type) -> {
        return RedisKey.getKeyStr(BizRedisKeyConstant.APP_DICT_KEY, appId);
    }),
    /**
     * redis 应用授权的组织机构缓存
     */
    APP_ORG("应用授权的组织机构", (appId, type) -> {
        return RedisKey.getKeyStr(BizRedisKeyConstant.APP_ORG_KEY, appId);
    }),

    /**
     * 轮播图
     */
    BANNER_LIST("轮播图", (type, lang) -> {
        return RedisKey.getKeyStr(BizRedisKeyConstant.BANNER_LIST, type, lang);
    }),
    /**
     * 服务器资源使用率记录
     */
    SERVER_USAGE_HISTORY("服务器资源使用率记录", (srType, type) -> {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(BizRedisKeyConstant.YYYYMMDD);
        return RedisKey.getKeyStr(BizRedisKeyConstant.SERVER_USAGE_HISTORY_PREFIX, srType, LocalDate.now().format(formatter));
    }),
    /**
     * 服务器资源使用记录
     */
    SERVER_USED_HISTORY("服务器资源使用记录", (srType, type) -> {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(BizRedisKeyConstant.YYYYMMDD);
        return RedisKey.getKeyStr(BizRedisKeyConstant.SERVER_USED_HISTORY_PREFIX, srType, LocalDate.now().format(formatter));
    }),
    /**
     * 服务器资源空闲记录
     */
    SERVER_FREE_HISTORY("服务器资源空闲记录", (srType, type) -> {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(BizRedisKeyConstant.YYYYMMDD);
        return RedisKey.getKeyStr(BizRedisKeyConstant.SERVER_FREE_HISTORY_PREFIX, srType, LocalDate.now().format(formatter));
    }),


    /**
     * 活动配置缓存锁
     */
    ACTIVITY_CONFIG_LOCK("活动配置缓存锁", (activity, p2) -> {
        return RedisKey.getKeyStr(BizRedisKeyConstant.ACTIVITY_CONFIG_LOCK, activity);
    }),
    /**
     * 活动配置缓存
     */
    ACTIVITY_CONFIG_LIST("活动配置缓存", (activity, p2) -> {
        return RedisKey.getKeyStr(BizRedisKeyConstant.ACTIVITY_CONFIG_LIST, activity);
    }),
    /**
     * 活动配置使用限额
     */
    ACTIVITY_CONFIG_QUOTA("活动配置使用限额", (activity, confId) -> {
        String date = DateFormatUtils.format(Calendar.getInstance(), BizRedisKeyConstant.YYYYMMDD);
        return RedisKey.getKeyStr(BizRedisKeyConstant.ACTIVITY_CONFIG_QUOTA, activity, confId, date);
    }),
    /**
     * 用户参与活动获得奖励
     */
    ACTIVITY_PLAY_INFO("用户参与活动获得奖励", (activity, userId) -> {
        String date = DateFormatUtils.format(Calendar.getInstance(), BizRedisKeyConstant.YYYYMMDD);
        return RedisKey.getKeyStr(BizRedisKeyConstant.ACTIVITY_PLAY_INFO, activity, date, userId);
    }),
    /**
     * 用户参与活动要求
     */
    ACTIVITY_PLAY_REQUIRE("用户参与活动要求", (activity, p2) -> {
        String date = DateFormatUtils.format(Calendar.getInstance(), BizRedisKeyConstant.YYYYMMDD);
        return RedisKey.getKeyStr(BizRedisKeyConstant.ACTIVITY_PLAY_REQUIRE, activity, date);
    }),
    /**
     * 每日首次赢得金豆达到10000的用户
     */
    ACTIVITY_PLAY_FIRST("每日首次赢得金豆达到10000的用户", (activity, p2) -> {
        String date = DateFormatUtils.format(Calendar.getInstance(), BizRedisKeyConstant.YYYYMMDD);
        return RedisKey.getKeyStr(BizRedisKeyConstant.ACTIVITY_PLAY_FIRST, activity, date);
    }),

    /**
     * 每日用户游戏累计金豆数量
     */
    GAME_REWARD_DAY("每日用户游戏累计金豆数量", (gameId, p2) -> {
        String date = DateFormatUtils.format(Calendar.getInstance(), BizRedisKeyConstant.YYYYMMDD);
        return RedisKey.getKeyStr(BizRedisKeyConstant.GAME_REWARD_DAY, gameId, date);
    }),
    /**
     * 每日首次游戏赢得金豆达到10000的用户
     */
    GAME_REWARD_10000("每日首次游戏赢得金豆达到10000的用户", (gameId, p2) -> {
        String date = DateFormatUtils.format(Calendar.getInstance(), BizRedisKeyConstant.YYYYMMDD);
        return RedisKey.getKeyStr(BizRedisKeyConstant.GAME_REWARD_10000, gameId, date);
    }),
    /**
     * redis 三方集成配置缓存
     */
    THIRD_CONFIG("三方集成配置缓存", (configType, groupId) -> {
        return RedisKey.getKeyStr(BizRedisKeyConstant.THIRD_CONFIG_KEY, configType, groupId);
    }),
    /**
     * redis 三方集成配置项缓存
     */
    THIRD_CONFIG_ITEM("三方集成配置项缓存", (groupId, itemType) -> {
        return RedisKey.getKeyStr(BizRedisKeyConstant.THIRD_CONFIG_ITEM_KEY, groupId, itemType);
    }),
    /**
     * redis 消息通知模板缓存
     */
    INFORM_TEMPLATE("消息通知模板缓存", (templateId, p2) -> {
        return RedisKey.getKeyStr(BizRedisKeyConstant.INFORM_TEMPLATE_KEY, templateId);
    }),
    /**
     * redis 消息通知事件缓存
     */
    INFORM_EVENT("消息通知事件缓存", (eventId, p2) -> {
        return RedisKey.getKeyStr(BizRedisKeyConstant.INFORM_EVENT_KEY, eventId);
    }),
    /**
     * redis 消息通知对象缓存
     */
    INFORM_TARGET("消息通知对象缓存", (type, eventId) -> {
        return RedisKey.getKeyStr(BizRedisKeyConstant.INFORM_TARGET_KEY, type, eventId);
    }),
    /**
     * redis 消息通知限制缓存
     */
    INFORM_SEND_LIMIT("消息通知限制缓存", (type, userId) -> {
        return RedisKey.getKeyStr(BizRedisKeyConstant.INFORM_SEND_LIMIT_KEY, type, userId);
    }),
    /**
     * redis 中国行政地区-下属行政区信息缓存
     */
    CNAREA_CHIRDREN("中国行政地区-下属行政区信息缓存", (pcode, p2) -> {
        return RedisKey.getKeyStr(BizRedisKeyConstant.CNAREA_2023_CHIRDREN_KEY, pcode);
    }),
    /**
     * redis 中国行政地区-省（直辖市）缓存
     */
    CNAREA_PROVINCE("中国行政地区-省（直辖市）缓存", (p1, p2) -> {
        return RedisKey.getKeyStr(BizRedisKeyConstant.CNAREA_2023_PROVINCE_KEY);
    }),

    ;

    private final String desc;
    private final BiFunction<Object, Object, String> function;

    BizRedisKey(String desc, BiFunction<Object, Object, String> function) {
        this.desc = desc;
        this.function = function;
    }

    public String getDesc() {
        return desc;
    }


    public static String REDIS_PREFIX = "rds";
    public final static String DELIMITER = ":";

    /**
     * 1、获取全名称key
     * @return 无参数组合后的redis缓存key
     */
    public String getKey() {
        return this.function.apply(null, null);
    }

    /**
     * 1、获取全名称key
     * @param key 缓存key的部分值
     * @return key参数组合后的redis缓存key
     */
    public String getKey(Object key) {
        return this.function.apply(key, null);
    }

    /**
     * 1、获取全名称key
     *
     * @param key1
     * @param key2
     * @return
     */
    public String getKey(Object key1, Object key2) {
        return this.function.apply(key1, key2);
    }

    public String getKey(Object key1, Object key2, Object key3) {
        return this.getKey(new Object[]{key1, key2, key3});
    }

    public static String getKeyStr(Object... args) {
        StringJoiner tempKey = new StringJoiner(DELIMITER);
        tempKey.add(REDIS_PREFIX);
        for (Object s : args) {
            if (Objects.isNull(s) || !StringUtils.hasText(s.toString())) {
                continue;
            }
            tempKey.add(s.toString());
        }
        return tempKey.toString();
    }

    public static String getThreadKeyStr(String prefix, Object... args) {

        StringJoiner tempKey = new StringJoiner(DELIMITER);
        tempKey.add(prefix);
        tempKey.add(String.valueOf(Thread.currentThread().getId()));
        for (Object s : args) {
            if (Objects.isNull(s) || !StringUtils.hasText(s.toString())) {
                continue;
            }
            tempKey.add(s.toString());
        }
        return tempKey.toString();
    }
}
