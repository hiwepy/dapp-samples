package com.github.hiwepy.dapp;

import com.blockchain.scanning.chain.model.TransactionModel;
import com.blockchain.scanning.monitor.TronMonitorEvent;

/**
 * 创建一个类，实现TronMonitorEvent接口即可
 */
public class TronEventOne implements TronMonitorEvent {

    /**
     * transactionModel 对象里包含此条交易的所有信息
     */
    @Override
    public void call(TransactionModel transactionModel) {
        System.out.println("TRON 成功了！！！");
        System.out.println("TRON, txID: " + transactionModel.getEthTransactionModel().getChainId());
    }

}
