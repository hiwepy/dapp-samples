package com.github.hiwepy.api.util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;

public class DateUtils {

    public static long utcConvertUnitMinutesAfterByZoneId(Long timestamp, Long minutes, TimezoneEnum timezoneEnum) {
        return utcConvertUnitMinutesByZoneId(timestamp + minutes * 60 * 1000, timezoneEnum);
    }

    public static long utcConvertUnitHourAfterByZoneId(Long timestamp, Long hour, TimezoneEnum timezoneEnum) {
        return utcConvertUnitHourByZoneId(timestamp + hour * 60 * 60 * 1000, timezoneEnum);
    }

    public static long utcConvertUnitDayAfterByZoneId(Integer timeClock, Long day, TimezoneEnum timezoneEnum) {
        long time = utcConvertUnitDayAndHourByZoneId(timeClock, timezoneEnum);
        return time + day * 24 * 60 * 60 * 1000;
    }

    public static long utcConvertUnitMinutesByZoneId(Long timestamp, TimezoneEnum timezoneEnum) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        ZonedDateTime zonedDateTime = instant.atZone(timezoneEnum.getZoneId());
        // 将秒和纳秒部分归零，并调整到最近的整10分钟
        ZonedDateTime truncated = zonedDateTime.truncatedTo(ChronoUnit.MINUTES);
        int minutesToAdjust = truncated.getMinute() % 10;
        ZonedDateTime nearestTenMinutes = truncated.minusMinutes(minutesToAdjust);

        return nearestTenMinutes.toInstant().toEpochMilli();
    }

    public static long utcConvertUnitHourByZoneId(Long timestamp, TimezoneEnum timezoneEnum) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        ZonedDateTime zonedDateTime = instant.atZone(timezoneEnum.getZoneId());
        // 将秒和纳秒部分归零，并调整到最近的整10分钟
        ZonedDateTime currentHour = zonedDateTime.truncatedTo(ChronoUnit.HOURS);

        return currentHour.toInstant().toEpochMilli();
    }

    public static long utcConvertUnitDayByZoneId(Long timestamp, TimezoneEnum timezoneEnum) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        ZonedDateTime zonedDateTime = instant.atZone(timezoneEnum.getZoneId());
        // 获取当日零点的时间
        ZonedDateTime midnightInUTC9 = zonedDateTime.toLocalDate().atStartOfDay(timezoneEnum.getZoneId());
        // 将ZonedDateTime转换为时间戳
        return midnightInUTC9.toInstant().toEpochMilli();
    }


    public static long utcConvertUnitDayAndHourByZoneId(Integer timeClock, TimezoneEnum timezoneEnum) {
        LocalDate today = LocalDate.now(timezoneEnum.getZoneId());
        LocalDateTime todayAt = LocalDateTime.of(today, LocalTime.of(timeClock, 0));
        ZonedDateTime zdt = todayAt.atZone(timezoneEnum.getZoneId());
        return zdt.toInstant().toEpochMilli();
    }

    public static long utcCurrentByZoneId(TimezoneEnum timezoneEnum) {
        ZonedDateTime nowInUTC9 = ZonedDateTime.now(timezoneEnum.getZoneId());
        // 将ZonedDateTime转换为时间戳
        return nowInUTC9.toInstant().toEpochMilli();
    }

    public static long utcCurrentHourByZoneId(Long timestamp, TimezoneEnum timezoneEnum) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        ZonedDateTime zonedDateTime = instant.atZone(timezoneEnum.getZoneId());
        // 将ZonedDateTime转换为时间戳
        return zonedDateTime.getHour();
    }


    public static long utcStartByZoneId(TimezoneEnum timezoneEnum) {
        // 获取当前时间
        ZonedDateTime nowInUTC9 = ZonedDateTime.now(timezoneEnum.getZoneId());
        // 获取当日零点的时间
        ZonedDateTime midnightInUTC9 = nowInUTC9.toLocalDate().atStartOfDay(timezoneEnum.getZoneId());
        // 将ZonedDateTime转换为时间戳
        return midnightInUTC9.toInstant().toEpochMilli();
    }

    public static long utcWeekStartByZoneId(TimezoneEnum timezoneEnum) {
        // 获取当前时刻的ZonedDateTime实例
        ZonedDateTime now = ZonedDateTime.now(timezoneEnum.getZoneId());
        // 调整到本周一并将时分秒设置为零
        ZonedDateTime startOfWeek = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                .withHour(0).withMinute(0).withSecond(0).withNano(0);

        return startOfWeek.toInstant().toEpochMilli();
    }


    public static String utcConvertYMDByZoneId(Long timestamp, TimezoneEnum timezoneEnum) {
        // 将时间戳转换为Instant对象
        Instant instant = Instant.ofEpochMilli(timestamp);
        ZonedDateTime zonedDateTime = instant.atZone(timezoneEnum.getZoneId());
        // 使用DateTimeFormatter格式化日期
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return zonedDateTime.format(formatter);
    }

    public static String utcConvertYMD1ByZoneId(Long timestamp, TimezoneEnum timezoneEnum) {
        // 将时间戳转换为Instant对象
        Instant instant = Instant.ofEpochMilli(timestamp);
        ZonedDateTime zonedDateTime = instant.atZone(timezoneEnum.getZoneId());
        // 使用DateTimeFormatter格式化日期
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        return zonedDateTime.format(formatter);
    }

    public static String utcConvertYMDFullByZoneId(Long timestamp, TimezoneEnum timezoneEnum) {
        // 将时间戳转换为Instant对象
        Instant instant = Instant.ofEpochMilli(timestamp);
        ZonedDateTime zonedDateTime = instant.atZone(timezoneEnum.getZoneId());
        // 使用DateTimeFormatter格式化日期
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        return zonedDateTime.format(formatter);
    }
    public static String utcConvertYMDZeroFullByZoneId(Long timestamp, TimezoneEnum timezoneEnum) {
        // 将时间戳转换为Instant对象
        Instant instant = Instant.ofEpochMilli(timestamp);
        ZonedDateTime zonedDateTime = instant.atZone(timezoneEnum.getZoneId());
        // 使用DateTimeFormatter格式化日期
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd 00:00:00");
        return zonedDateTime.format(formatter);
    }

    public static String utcConvertYMDHHFullByZoneId(Long timestamp, TimezoneEnum timezoneEnum) {
        // 将时间戳转换为Instant对象
        Instant instant = Instant.ofEpochMilli(timestamp);
        ZonedDateTime zonedDateTime = instant.atZone(timezoneEnum.getZoneId());
        // 使用DateTimeFormatter格式化日期
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHH");
        return zonedDateTime.format(formatter);
    }


    public static Long YMDHHFullConvertUtcByZoneId(String timestamp, TimezoneEnum timezoneEnum) {
        // 使用DateTimeFormatter格式化日期
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHH");
        // 将字符串转换为LocalDateTime对象
        LocalDateTime dateTime = LocalDateTime.parse(timestamp, formatter);
        // 将LocalDateTime转换为ZonedDateTime，考虑指定时区
        ZonedDateTime zonedDateTime = dateTime.atZone(timezoneEnum.getZoneId());

        // 将ZonedDateTime转换为指定时区的时间戳
        return zonedDateTime.toEpochSecond() * 1000;
    }

    public static Long YMDFullConvertUtcByZoneId(String timestamp, TimezoneEnum timezoneEnum) {
        // 使用DateTimeFormatter格式化日期
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // 将字符串转换为LocalDateTime对象
        LocalDateTime dateTime = LocalDateTime.parse(timestamp, formatter);
        // 将LocalDateTime转换为ZonedDateTime，考虑指定时区
        ZonedDateTime zonedDateTime = dateTime.atZone(timezoneEnum.getZoneId());

        // 将ZonedDateTime转换为指定时区的时间戳
        return zonedDateTime.toEpochSecond() * 1000;
    }


    public static String utcConvertYMDFullByZoneIdAndDiffTime(Long timestamp, Integer diffTime, TimezoneEnum timezoneEnum) {
        // 将时间戳转换为Instant对象
        Instant instant = Instant.ofEpochMilli(timestamp - diffTime);
        ZonedDateTime zonedDateTime = instant.atZone(timezoneEnum.getZoneId());
        // 使用DateTimeFormatter格式化日期
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        return zonedDateTime.format(formatter);
    }

}
