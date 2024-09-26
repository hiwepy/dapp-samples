-- ----------------------------
-- Table structure for t_chain_transaction
-- ----------------------------
CREATE TABLE `t_chain_transaction` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `chain_id` varchar(50) NOT NULL COMMENT 'chainId',
    `hash` varchar(50) NOT NULL COMMENT '交易哈希',
    `from` varchar(50) NOT NULL COMMENT '发送交易的钱包地址',
    `to` varchar(50) NOT NULL COMMENT '目的地钱包地址',
    `value` decimal(16,9) NOT NULL DEFAULT '0.000000000' COMMENT '价值，发送到目的地的Toncoin数量',
    `gas_limit` int(11) DEFAULT '0' COMMENT 'Gas 限制',
    `gas_used` int(11) DEFAULT '0' COMMENT 'Gas 消耗',
    `gas_price` decimal(16,18) NOT NULL DEFAULT '0.0000' COMMENT 'Gas 价格',
    `gas_fees` datetime NOT NULL COMMENT 'Gas 费用',
    `cost` decimal(16,8) NOT NULL DEFAULT '0.0000' COMMENT '交易成本',
    `nonce` int(11) DEFAULT '0' COMMENT '随机数',
    `data` text NULL COMMENT '输入数据',
    `status` int(2) DEFAULT '0' COMMENT '交易状态',
    `created_at` bigint NOT NULL DEFAULT '0' COMMENT '交易状态',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_user` (`from`) USING BTREE COMMENT '链上签到记录索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='链上每日用户签到记录表';

