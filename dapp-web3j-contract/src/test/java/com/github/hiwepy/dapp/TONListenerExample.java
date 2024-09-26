package com.github.hiwepy.dapp;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.http.HttpService;

public class TONListenerExample {
    public static void main(String[] args) throws Exception {
        // 初始化Web3j实例，连接到TON节点的JSON-RPC API
        Web3j web3j = Web3j.build(new HttpService("http://your-ton-node-url:port"));

        DefaultBlockParameter.valueOf("latest");
       /* // 创建一个过滤器来监听所有的区块和交易
        EthFilter filter = new EthFilter(DefaultBlockParameterName.EARLIEST, DefaultBlockParameterName.LATEST, null);

        // 订阅过滤器，开始监听
        web3j.ethLogFlowable(filter).subscribe(log -> {
            // 处理收到的日志
            Log ethLog = (Log) log;
            // 你可以进一步处理日志，例如检查交易或区块信息
            log
            // 打印日志
            System.out.println("Log: " + ethLog);
        });*/

        // 注意：这个例子会持续监听，直到程序被中断。
        // 在实际应用中，你可能需要更复杂的逻辑来管理订阅和取消订阅。
    }
}