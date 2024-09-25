package com.github.hiwepy.ton4j.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public enum UserTypeEnum {

    /**
     * 普通用户
     */
    GENERAL("GENERAL"),
    /**
     * 代理用户
     */
    AGENT("AGENT"),
    ;

    private final String code;

    public static UserTypeEnum fromValue(String bar) {
        for (UserTypeEnum deliveryTypeEnum : UserTypeEnum.values()) {
            if (bar.equalsIgnoreCase(deliveryTypeEnum.getCode())) {
                return deliveryTypeEnum;
            }
        }
        return null;
    }

}
