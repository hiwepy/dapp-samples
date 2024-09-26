package com.github.hiwepy.dapp.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
     * 钱包地址
     */
    private String address;

    /**
     * 交易hash地址
     */
    private String hashAddress;

    /**
     * 签到时间
     */
    private Long signInAt;

    /**
     * 创建时间
     */
    private Long createdAt;

    /**
     * 更新时间
     */
    private Long updatedAt;

    private static final long serialVersionUID = 1L;

    public static ChainDailySignLog buildChainDailySignLog(Long userId, Long userClientId, String address, String hashAddress) {
        long now = Instant.now().toEpochMilli();
        return ChainDailySignLog.builder()
                .userId(userId)
                .userClientId(String.valueOf(userClientId))
                .address(address)
                .hashAddress(hashAddress)
                .signInAt(now)
                .createdAt(now)
                .updatedAt(now)
                .build();
    }

}
