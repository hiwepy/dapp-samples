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
 * 链上每日用户签到表
 *
 * @TableName t_chain_daily_sign_user
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@TableName(value = "t_chain_daily_sign_user")
public class ChainDailySignUser implements Serializable {

    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 版本
     */
    private Integer version;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 持续天数
     */
    private Integer durationDays;

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

    public static ChainDailySignUser buildChainDailySignUserSave(Long userId, int durationDays) {
        long now = Instant.now().toEpochMilli();
        return ChainDailySignUser.builder()
                .version(1)
                .userId(userId)
                .durationDays(durationDays)
                .lastSignInAt(now)
                .createdAt(now)
                .updatedAt(now)
                .build();
    }

    public static ChainDailySignUser buildChainDailySignUserUpdate(ChainDailySignUser already, int durationDays) {
        long now = Instant.now().toEpochMilli();
        return ChainDailySignUser.builder()
                .id(already.getId())
                .oldVersion((already.getVersion()))
                .version(already.getVersion())
                .durationDays(durationDays)
                .lastSignInAt(now)
                .updatedAt(now)
                .build();
    }
}
