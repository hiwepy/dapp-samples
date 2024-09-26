package com.github.hiwepy.api.constant;


public abstract class RedisKeyConstant {

    /**
     * 用户坐标缓存
     */
    public final static String GEO_LOCATION_KEY = "geo:location";
    /**
     * IP坐标缓存
     */
    public final static String IP_REGION_KEY = "ip:region";
    /**
     * IP坐标缓存
     */
    public final static String IP_LOCATION_KEY = "ip:location";
    /**
     * IP坐标缓存（百度服务缓存）
     */
    public final static String IP_BAIDU_LOCATION_KEY = "baidu:ip:location";
    /**
     * IP坐标缓存（太平洋网络）
     */
    public final static String IP_PCONLINE_LOCATION_KEY = "pconline:ip:location";
    /**
     * 消息缓存Key
     */
    public final static String MESSAGE_ID_KEY = "message:id";
    /**
     * 消息重试次数缓存Key
     */
    public final static String MESSAGE_RETRY_KEY = "message:retry";
}
