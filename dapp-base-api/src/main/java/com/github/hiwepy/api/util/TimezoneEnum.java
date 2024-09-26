package com.github.hiwepy.api.util;

import java.time.ZoneId;

public enum TimezoneEnum {
    UTC("UTC"),
    TOKYO("Asia/Tokyo"),
    LONDON("Europe/London"),
    NEW_YORK("America/New_York"),
    SYDNEY("Australia/Sydney"),
    PARIS("Europe/Paris"),
    SHANGHAI("Asia/Shanghai"),
    MOSCOW("Europe/Moscow"),
    LOS_ANGELES("America/Los_Angeles"),
    KOLKATA("Asia/Kolkata");

    private String zoneId;

    TimezoneEnum(String zoneId) {
        this.zoneId = zoneId;
    }

    public ZoneId getZoneId() {
        return ZoneId.of(zoneId);
    }
}
