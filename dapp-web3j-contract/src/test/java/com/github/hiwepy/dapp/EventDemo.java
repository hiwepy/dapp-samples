package com.github.hiwepy.dapp;

import com.blockchain.scanning.MagicianBlockchainScan;
import com.blockchain.scanning.biz.thread.EventThreadPool;
import com.blockchain.scanning.chain.model.TransactionModel;
import com.blockchain.scanning.commons.enums.ChainType;
import com.blockchain.scanning.monitor.EthMonitorEvent;
import com.blockchain.scanning.monitor.filter.EthMonitorFilter;

import java.math.BigInteger;

/**
 * 创建一个类，实现 EthMonitorEvent 接口 即可
 */
public class EventDemo implements EthMonitorEvent {

    /**
     * 筛选条件，如果遇到了符合条件的交易，会自动触发 call 方法
     * 这些条件都是 并且的关系，必须要同时满足才行
     * 如果不想根据某个条件筛选，直接不给那个条件设置值就好了
     * 这个方法如果不实现，或者返回 null ， 那么就代表监听任意交易
     */
    @Override
    public EthMonitorFilter ethMonitorFilter() {
        return EthMonitorFilter.builder()
                .setFromAddress("0x131231249813d334C58f2757037F68E2963C4crc") // 筛选 fromAddress 发送的交易
                .setToAddress("0x552115849813d334C58f2757037F68E2963C4c5e") // 筛选 toAddress 或 合约地址 收到的交易
                .setMinValue(BigInteger.valueOf(1)) // 筛选发送的主链币数量 >= minValue 的交易
                .setMaxValue(BigInteger.valueOf(10)) // 筛选发送的主链币数量 <= maxValue 的交易
                .setFunctionCode("0xasdas123"); // 筛选调用合约内 某方法 的交易
    }

    /**
     * 如果遇到了符合上面条件的交易，就会触发这个方法
     * transactionModel.getEthTransactionModel() 是一个交易对象，内部包含 hash ，value ，from ，to 等 所有的数据
     */
    @Override
    public void call(TransactionModel transactionModel) {
        String template = "EventOne 扫描到了, hash:{0}, from:{1}, to: {2}, input: {3}";
        template = template.replace("{0}", transactionModel.getEthTransactionModel().getBlockHash());
        template = template.replace("{1}", transactionModel.getEthTransactionModel().getFrom());
        template = template.replace("{2}", transactionModel.getEthTransactionModel().getTo());
        template = template.replace("{3}", transactionModel.getEthTransactionModel().getInput());

        System.out.println(template);
    }

    /**
     * https://learnblockchain.cn/article/5001
     * @param args
     */
    public static void main(String[] args) throws Exception {
        // 初始化线程池，核心线程数必须 >= 扫块的任务数量，建议等于扫块的任务数量
        EventThreadPool.init(1);

        // 开启一个扫块任务，如果你想扫描多个链，那么直接拷贝这段代码，并修改配置即可
        MagicianBlockchainScan.create()
                .setRpcUrl("https://data-seed-prebsc-1-s1.binance.org:8545/") // 节点的 RPC 地址
                .setChainType(ChainType.ETH) // 要扫描的链（如果设置成 ETH ，那么可以扫描 BSC, POLYGAN 等其他任意 以太坊标准的链）
                .setScanPeriod(5000) // 每轮扫描的间隔
                .setScanSize(1000) // 每轮扫描的块数
                .setBeginBlockNumber(BigInteger.valueOf(24318610)) // 从哪个块高开始扫描
                .addEthMonitorEvent(new EventDemo()) // 添加 监听事件
                //.addEthMonitorEvent(new EventTwo()) // 添加 监听事件
               // .addEthMonitorEvent(new EventThree()) // 添加 监听事件
                .start();
    }
}
