package com.github.hiwepy.ton4j.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;

import java.io.Serializable;
import java.time.Instant;

/**
 * 链上每日用户签到记录表
 *
 * @TableName t_chain_daily_sign_log
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@TableName(value = "t_chain_daily_sign_log")
public class ChainDailySignLog implements Serializable {

    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 用户ID（关联用户表的ID）
     */
    private Long userId;

    /**
     * 客户端id
     */
    private String userClientId;

    /**
     * token
     */
    private String tokenId;

    /**
     * 链 token
     */
    private String chainTokenId;

    /**
     * 签到时间
     */
    private Lo signTime;

    /**
     * 最后一次签到时间
     */
    private Long lastSignInAt;

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

    public static ChainDailySignLog buildActivityDailySignUserSave(long userId, int durationDays) {
        long now = Instant.now().toEpochMilli();
        return ChainDailySignLog.builder()
                .version(1)
                .userId(userId)
                .durationDays(durationDays)
                .lastSignInAt(now)
                .createdAt(now)
                .updatedAt(now)
                .build();
    }

    public static ChainDailySignLog buildActivityDailySignUserUpdate(ChainDailySignLog already, int durationDays) {
        long now = Instant.now().toEpochMilli();
        return ChainDailySignLog.builder()
                .id(already.getId())
                .oldVersion((already.getVersion()))
                .version(already.getVersion())
                .durationDays(durationDays)
                .lastSignInAt(now)
                .updatedAt(now)
                .build();
    }
}
