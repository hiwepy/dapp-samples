package com.github.hiwepy.api.constant;


import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.StringJoiner;
import java.util.function.Function;

public enum RedisKey {

    /**
     * 用户坐标缓存
     */
    GEO_LOCATION_KEY("用户坐标", (userId)->{
        return getKeyStr(RedisKeyConstant.GEO_LOCATION_KEY);
    }),
    /**
     * IP地区编码缓存
     */
    IP_REGION_INFO("用户坐标对应的地区编码缓存", (ip)->{
        return getKeyStr(RedisKeyConstant.IP_REGION_KEY, ip);
    }),
    /**
     * IP坐标缓存
     */
    IP_LOCATION_INFO("用户坐标对应的地理位置缓存", (ip)->{
        return getKeyStr(RedisKeyConstant.IP_LOCATION_KEY, ip);
    }),
    /**
     * IP坐标缓存（百度服务缓存）
     */
    IP_LOCATION_BAIDU_INFO("IP坐标缓存（百度服务缓存）", (ip)->{
        return getKeyStr(RedisKeyConstant.IP_BAIDU_LOCATION_KEY, ip);
    }),
    /**
     * IP坐标缓存（太平洋网络）
     */
    IP_LOCATION_PCONLINE_INFO("IP坐标缓存（太平洋网络）", (ip)->{
        return getKeyStr(RedisKeyConstant.IP_PCONLINE_LOCATION_KEY, ip);
    }),
    /**
     * 业务对应的消息缓存
     */
    MESSAGE_ID("业务对应的消息缓存", (msgId) -> {
        return getKeyStr(RedisKeyConstant.MESSAGE_ID_KEY, msgId);
    }),
    MESSAGE_RETRY("业务对应的消息重试次数缓存", (msgId) -> {
        return RedisKey.getKeyStr(RedisKeyConstant.MESSAGE_RETRY_KEY, msgId);
    }),

    ;

    private String desc;
    private Function<Object, String> function;

    RedisKey(String desc, Function<Object, String> function) {
        this.desc = desc;
        this.function = function;
    }

    public String getDesc() {
        return desc;
    }

    /**
     * 1、获取全名称key
     * @return 无参数组合后的redis缓存key
     */
    public String getKey() {
        return this.function.apply(null);
    }

    /**
     * 1、获取全名称key
     * @param key 缓存key的部分值
     * @return key参数组合后的redis缓存key
     */
    public String getKey(Object key) {
        return this.function.apply(key);
    }

    public static String REDIS_PREFIX = "rds";
    public final static String DELIMITER = ":";

    public static String getKeyStr(Object... args) {
        return getKeyStr(REDIS_PREFIX, args);
    }

    public static String getKeyStr(String prefix, Object... args) {
        StringJoiner tempKey = new StringJoiner(DELIMITER);
        tempKey.add(prefix);
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

    public static void main(String[] args) {
        System.out.println(getKeyStr(233,""));
    }


}
