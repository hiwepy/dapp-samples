package com.github.hiwepy.dapp.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 链上每日签到奖励配置表
 * @TableName t_config_chain_daily_sign
 */
@Data
@TableName(value = "t_config_chain_daily_sign")
public class ConfigChainDailySign implements Serializable {

    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 版本
     */
    @TableField("version")
    private Integer version;

    /**
     * 天数
     */
    @TableField("day")
    private Integer day;

    /**
     * 奖励
     */
    @TableField("reward")
    private BigDecimal reward;

    /**
     * 创建时间
     */
    @TableField("created_at")
    private Long createdAt;

    /**
     * 更新时间
     */
    @TableField("updated_at")
    private Long updatedAt;

    private static final long serialVersionUID = 1L;
}
