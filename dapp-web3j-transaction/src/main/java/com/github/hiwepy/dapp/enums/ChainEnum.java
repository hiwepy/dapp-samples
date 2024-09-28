package com.github.hiwepy.dapp.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Getter
@RequiredArgsConstructor
public enum ChainEnum {

    /**
     * Scroll 链
     */
    SCROLL(10000L, 10001L, true),
    /**
     * Ton 链
     */
    TON(20000L, 20001L, true),
    ;
    public final Long mainChainId;
    public final Long testChainId;
    public final Boolean isOn;

    public static List<ChainEnum> on() {
        return Arrays.stream(ChainEnum.values()).filter(p -> p.isOn).collect(Collectors.toList());
    }

    public static ChainEnum ofValue(Long chainId) {
        for (ChainEnum value : ChainEnum.values()) {
            if (chainId.equals(value.mainChainId) || chainId.equals(value.testChainId)) {
                return value;
            }
        }
        return null;
    }


    public static boolean isScroll(Long chainId, String env) {
        if (EnvironmentEnum.PROD.name().equalsIgnoreCase(env.trim())) {
            return SCROLL.mainChainId.equals(chainId);
        } else {
            return SCROLL.testChainId.equals(chainId);
        }
    }

    public static boolean isEIP155(Long chainId) {
        if (SCROLL.mainChainId.equals(chainId) ||
                SCROLL.testChainId.equals(chainId)) {
            return true;
        }
        return false;
    }

    public static long chainId(String env) {
        if (EnvironmentEnum.PROD.name().equalsIgnoreCase(env.trim())) {
            return SCROLL.mainChainId;
        } else {
            return SCROLL.testChainId;
        }
    }

}
