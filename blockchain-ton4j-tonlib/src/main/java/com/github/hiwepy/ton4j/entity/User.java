package com.github.hiwepy.ton4j.entity;

import com.github.hiwepy.api.geo.GeoLocation;
import com.github.hiwepy.ton4j.enums.UserTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class User implements Serializable {

    /**
     * 主键
     */
    private Long id;

    /**
     * 版本
     */
    private Integer version;

    /**
     * 用户
     */
    private String clientUserId;

    /**
     * 类型
     */
    private String type;

    /**
     * IP
     */
    private String ip;

    /**
     * Country
     */
    private String country;
    private String userName;
    private String firstName;
    private String lastName;
    private String languageCode;

    /**
     * city
     */
    private String city;

    /**
     * region
     */
    private String region;

    /**
     * latitude
     */
    private String latitude;

    /**
     * longitude
     */
    private String longitude;

    /**
     * 状态
     */
    private Integer status;
    private Integer level;

    /**
     * 创建时间
     */
    private Long createdAt;

    /**
     * 更新时间
     */
    private Long updatedAt;

    @Transient
    private Integer oldVersion;


    private static final long serialVersionUID = 1L;

    public static User buildInsert(String clientUserId, String ip, GeoLocation location, String userName, String firstName, String lastName, String languageCode) {
        long now = Instant.now().toEpochMilli();
        return User.builder()
                .version(1)
                .clientUserId(clientUserId)
                .ip(ip)
                .country(location.getCountry())
                .userName(Objects.isNull(userName) ? "" : userName)
                .firstName(Objects.isNull(firstName) ? "" : firstName)
                .lastName(Objects.isNull(lastName) ? "" : lastName)
                .languageCode(Objects.isNull(languageCode) ? "" : languageCode)
                .city(location.getCity())
                .region(location.getRegion())
                .latitude(location.getLatitude().toString())
                .longitude(location.getLongitude().toString())
                .type(UserTypeEnum.GENERAL.getCode())
                .createdAt(now)
                .updatedAt(now)
                .build();

    }

    public static User buildUpdate(User already, String ip, GeoLocation location, String userName, String firstName, String lastName, String languageCode) {
        return User.builder()
                .id(already.getId())
                .oldVersion((already.getVersion()))
                .version(already.getVersion() + 1)
                .ip(ip)
                .country(location.getCountry())
                .userName(Objects.isNull(userName) ? "" : userName)
                .firstName(Objects.isNull(firstName) ? "" : firstName)
                .lastName(Objects.isNull(lastName) ? "" : lastName)
                .languageCode(Objects.isNull(languageCode) ? "" : languageCode)
                .city(location.getCity())
                .region(location.getRegion())
                .latitude(location.getLatitude().toString())
                .longitude(location.getLongitude().toString())
                .updatedAt(Instant.now().toEpochMilli())
                .build();

    }

    public static User buildUpdateLevel(User already, int level) {
        return User.builder()
                .id(already.getId())
                .oldVersion((already.getVersion()))
                .version(already.getVersion())
                .level(level)
                .updatedAt(Instant.now().toEpochMilli())
                .build();

    }
}
