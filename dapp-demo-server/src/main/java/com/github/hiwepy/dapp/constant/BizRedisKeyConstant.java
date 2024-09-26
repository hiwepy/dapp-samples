package com.github.hiwepy.dapp.constant;

import com.github.hiwepy.api.constant.RedisKeyConstant;

public class BizRedisKeyConstant extends RedisKeyConstant {

    public final static String DELIMITER = ":";
    public final static String YYYYMMDD = "yyyyMMdd";
    public final static String YYYYMM = "yyyyMM";
    public final static String YYYY = "yyyy";

    /**
     * 用户token过期时间
     */
    public final static Long TOKEN_EXPIRE = 7 * 86400L;
    /**
     * 锁过期时间5秒钟
     */
    public final static Long FIVE_SECONDS = 5 * 1000L;



    /**
     * 应用黑名单缓存
     */
    public final static String APP_BLACKLIST_KEY = "app:blacklist";
    /**
     * 数据字典缓存
     */
    public final static String APP_DICT_KEY = "app:dict";
    /**
     * 应用授权的组织机构缓存
     */
    public final static String APP_ORG_KEY = "app:org";
    /**
     * redis 用户登陆注册状态锁
     */
    public final static String USER_LOCK_REG_KEY = "user:lock:reg";
    /**
     * redis 用户登陆次数
     */
    public final static String USER_LOGIN_AMOUNT_KEY = "user:login:amount";
    /**
     * redis 用户登陆信息
     */
    public final static String USER_LOGIN_INFO_KEY = "user:login:info";
    /**
     * redis 用户登陆身份信息
     */
    public final static String USER_LOGIN_IDENTITY_LIST_KEY = "user:login:identityList";
    /**
     * redis 用户登陆关联子账号信息
     */
    public final static String USER_LOGIN_SUB_ACCOUNT_LIST_KEY = "user:login:subAccountList";
    /**
     * redis 用户登陆状态信息
     */
    public final static String USER_LOGIN_STATUS_KEY = "user:login:status";
    /**
     * redis 用户 token
     */
    public final static String USER_TOKEN_KEY = "user:token";
    /**
     * User Token 的 rsa 私钥缓存Key
     */
    public final static String USER_TOKEN_PRI_KEY = "user:token:rsa:pri";
    /**
     * User Token 的 rsa 公钥缓存Key
     */
    public final static String USER_TOKEN_PUB_KEY = "user:token:rsa:pub";
    /**
     * redis 用户黑名单缓存
     */
    public final static String USER_BLACKLIST_KEY = "user:blacklist";
    /**
     * redis 我关注的用户缓存
     */
    public final static String USER_FOLLOW_KEY = "user:follow";
    /**
     * redis 关注我的用户缓存
     */
    public final static String USER_FOLLOWERS_KEY = "user:followers";
    /**
     * redis 新关注我的用户缓存
     */
    public final static String USER_NEW_FOLLOWERS_KEY = "user:new:followers";
    /**
     * redis 用户账号前缀
     */
    public final static String USER_ACCOUNT_KEY = "user:account";
    /**
     * redis 用户code前缀
     */
    public final static String USER_CODE_KEY = "user:code";
    /**
     * redis 用户关联孩子缓存
     */
    public final static String USER_CHILD_LIST_KEY = "user:child";
    /**
     * redis 用户信息前缀
     */
    public final static String USER_INFO_KEY = "user:info";
    /**
     * redis 用户角色前缀
     */
    public final static String USER_ROLE_KEY = "user:role";
    /**
     * redis 用户权限前缀
     */
    public final static String USER_PERMS_KEY = "user:perms";
    /**
     * redis 所有用户ID信息前缀
     */
    public final static String USER_LIST_KEY = "user:info:list";
    /**
     * redis 指定用户组前缀
     */
    public final static String USER_SPECIFIC_KEY = "user:specific";
    /**
     * redis 用户单点登录状态
     */
    public final static String USER_SSO_STATE_KEY = "user:sso";
    /**
     * 用户会话列表
     */
    public final static String USER_SESSIONS_KEY = "user:sessions";
    /**
     * 用户会话信息
     */
    public final static String USER_SESSION_KEY = "user:session";
    /**
     * 用户坐标缓存
     */
    public final static String USER_GEO_LOCATION_KEY = "user:geo:location";
    /**
     * 用户坐标对应的地理位置缓存
     */
    public final static String USER_GEO_INFO_KEY = "user:geo:info";
    /**
     * 用户资产同步锁
     */
    public final static String USER_CAPITAL_DUMP_LOCK = "user:capital:dump:lock";
    /**
     * 用户任务列表
     */
    public final static String USER_TASK_KEY = "user:task";
    /**
     * 用户资产缓存
     */
    public final static String USER_ASSETS_AMOUNT_KEY = "user:assets:amount";
    /**
     * 用户资产缓存
     */
    public final static String USER_ASSETS_TOKEN_KEY = "user:assets:token";
    /**
     * 用户资产换算临时缓存
     */
    public final static String USER_EXCHANGE_AMOUNT = "user:exchange:amount";
    /**
     * 用户金币增量缓存
     */
    public final static String USER_COIN_AMOUNT_KEY = "user:coin:amount";
    /**
     * 用户珍珠增量缓存
     */
    public final static String USER_PEARL_AMOUNT_KEY = "user:pearl:amount";
    /**
     * 用户经验增量缓存
     */
    public final static String USER_EXP_AMOUNT_KEY = "user:exp:amount";
    /**
     * 用户会员权益缓存
     */
    public final static String USER_RITHTS_KEY = "user:rights";
    /**
     * 消息队列消息消费锁
     */
    public final static String MQ_CONSUME_LOCK = "mq:consume:lock";

    /**
     * 接口幂等缓存（Token模式）
     */
    public final static String IDEMPOTENT_TOKEN_KEY = "idempotent:token";
    /**
     * 接口幂等缓存（参数模式）
     */
    public final static String IDEMPOTENT_ARGS_KEY = "idempotent:args";


    /**
     * 轮播图
     */
    public static final String BANNER_LIST = "banner";

    /**
     * 服务器资源使用率记录
     */
    public final static String SERVER_USAGE_HISTORY_PREFIX = "server:usage";
    /**
     * 服务器资源使用记录
     */
    public final static String SERVER_USED_HISTORY_PREFIX = "server:used";
    /**
     * 服务器资源空闲记录
     */
    public final static String SERVER_FREE_HISTORY_PREFIX = "server:free";

    public final static String SET_APPS = "user:apps";

    public final static String DBMATA_CATALOG = "dbmata:catalog";
    public final static String DBMATA_CATALOG_LODING = "dbmata:catalog:loding";


    /**
     * 活动配置缓存更新锁
     */
    public static final String ACTIVITY_CONFIG_LOCK = "activity:lock";

    /**
     * 活动配置缓存
     */
    public static final String ACTIVITY_CONFIG_LIST = "activity:config";
    /**
     * 活动配置使用限额
     */
    public static final String ACTIVITY_CONFIG_QUOTA = "activity:quota";
    /**
     * 用户参与活动获得奖励、剩余开袋次数
     */
    public static final String ACTIVITY_PLAY_INFO = "activity:play:info";
    /**
     * 用户参与活动要求
     * 1、活动期间，参与任意直播间游戏可获得开袋机会
     * 2、每日首次赢得金豆数达到10000时可以获得一次开袋机会，之后每赢得100000金豆可以获得一次开袋机
     */
    public static final String ACTIVITY_PLAY_REQUIRE = "activity:play:require";
    /**
     * 每日首次赢得金豆达到10000的用户
     */
    public static final String ACTIVITY_PLAY_FIRST = "activity:play:first";
    /**
     * 每日用户游戏累计金豆数量
     */
    public static final String GAME_REWARD_DAY = "game:reward:daily";
    /**
     * 游戏遮罩引导确认缓存
     */
    public static final String GAME_REWARD_10000 = "game:reward:10000";

    /**
     * redis 三方集成配置缓存Key前缀
     */
    public final static String THIRD_CONFIG_KEY = "third:config";
    /**
     * redis 三方集成配置项缓存Key前缀
     */
    public final static String THIRD_CONFIG_ITEM_KEY = "third:config-item";
    /**
     * redis 消息通知模板缓存Key前缀
     */
    public final static String INFORM_TEMPLATE_KEY = "inform:template";
    /**
     * redis 消息通知模板缓存Key前缀
     */
    public final static String INFORM_EVENT_KEY = "inform:event";
    /**
     * redis 消息通知对象缓存Key前缀
     */
    public final static String INFORM_TARGET_KEY = "inform:target";
    /**
     * redis 消息通知限制缓存Key前缀
     */
    public final static String INFORM_SEND_LIMIT_KEY = "inform:send:limit";
    /**
     * redis 中国行政地区-下属行政区信息缓存Key前缀
     */
    public final static String CNAREA_2023_CHIRDREN_KEY = "cnarea:2023:chirdren";
    /**
     * redis 中国行政地区-省（直辖市）缓存Key前缀
     */
    public final static String CNAREA_2023_PROVINCE_KEY = "cnarea:2023:province";
}
