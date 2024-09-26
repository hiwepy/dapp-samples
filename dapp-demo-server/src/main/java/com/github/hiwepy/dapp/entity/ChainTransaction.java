package com.github.hiwepy.dapp.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * 链上交易表
 *
 * @TableName t_chain_transaction
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@TableName(value = "t_chain_transaction")
public class ChainTransaction {

    /**
     * 交易哈希
     */
    private String hash;
    /**
     * 随机数
     */
    private String nonce;
    /**
     * 区块哈希
     */
    private String blockHash;
    /**
     * 区块
     */
    private String blockNumber;
    /**
     * 区块链ID
     */
    private String chainId;
    /**
     * 发送交易的钱包地址
     */
    private String from;
    /**
     * 目的地钱包地址
     */
    private String to;
    /**
     * 价值，发送到目的地的Toncoin数量
     */
    private BigInteger value;
    /**
     * Gas 限制
     */
    private BigInteger gasLimit;
    /**
     * Gas 价格
     */
    private BigInteger gasPrice;
    /**
     * Gas 消耗
     */
    private BigInteger gasUsed;
    /**
     * Gas 费用
     */
    private BigInteger gasFees;
    /**
     * 交易成本
     */
    private BigDecimal cost;
    /**
     * 输入数据
     */
    private String data;
    private String maxFeePerGas;
    private String maxPriorityFeePerGas;
    private String maxFeePerBlobGas;
    /**
     * 交易状态
     */
    String status;
    /**
     * 创建时间
     */
    @TableField("created_at")
    private Long createdAt;
}
