-- ----------------------------
-- Table structure for t_chain_trade_log
-- ----------------------------
CREATE TABLE `t_chain_daily_sign_log` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_id` bigint(20) NOT NULL COMMENT '用户ID（关联用户表的ID）',
    `tx_hash` varchar(50) NOT NULL COMMENT '钱包地址',
    `from` varchar(50) NOT NULL COMMENT '交易hash地址',
    `to` varchar(50) NOT NULL COMMENT '交易hash地址',
    `value` decimal(16,8) NOT NULL DEFAULT '0.0000' COMMENT '签到奖励',
    `gas_limit` int(11) DEFAULT '0' COMMENT '连续签到天数',
    `gas_used` int(11) DEFAULT '0' COMMENT '连续签到天数',
    `gas_price` decimal(16,8) NOT NULL DEFAULT '0.0000' COMMENT '签到奖励',
    `gas_fees` datetime NOT NULL COMMENT '签到时间',
    `tx_cost` decimal(16,8) NOT NULL DEFAULT '0.0000' COMMENT '签到奖励',
    `nonce` int(11) DEFAULT '0' COMMENT '连续签到天数',
    `input_data` text NULL COMMENT '输入数据',
    `status` int(2) DEFAULT '0' COMMENT '连续签到天数',
    `created_at` bigint NOT NULL DEFAULT '0' COMMENT '交易状态',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_user` (`user_id`) USING BTREE COMMENT '链上签到记录索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='链上每日用户签到记录表';

