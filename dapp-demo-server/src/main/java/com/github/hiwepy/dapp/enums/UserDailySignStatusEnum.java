package com.github.hiwepy.dapp.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public enum UserDailySignStatusEnum {
    NOT_TRADE,
    WAIT_CLAIM,
    CLAIMED,

    ;

}
