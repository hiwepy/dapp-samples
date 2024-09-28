package com.github.hiwepy.dapp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.EthBlockNumber;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.ClientTransactionManager;
import org.web3j.tx.ManagedTransaction;
import org.web3j.tx.TransactionManager;

import java.io.IOException;
import java.math.BigInteger;

/**
 * 智能合約
 */
@Configuration
public class ContractConfiguration {

    //智能合约部署地址
    private String contractAddress = "0xaf0895260ef377ceea0086c99e3eff7999f742c9";

    @Bean
    @Scope("prototype")
    public Web3j web3j() {
        return Web3j.build(new HttpService("http://192.168.0.104:15450"));
    }

    @Bean
    @Autowired
    public Trace trace(Web3j web3j) throws IOException {
        Trace contract;
        try {
            Web3ClientVersion web3ClientVersion = web3j.web3ClientVersion().send();
            String clientVersion = web3ClientVersion.getWeb3ClientVersion();
            System.out.println("clientVersion" + clientVersion);
            // 以某个用户的身份调用合约
            TransactionManager transactionManager = new ClientTransactionManager(web3j, "0x24602722816b6cad0e143ce9fabf31f6026ec622");
            // 加载智能合约
            contract = Trace.load(contractAddress, web3j, transactionManager, ManagedTransaction.GAS_PRICE, org.web3j.tx.Contract.GAS_LIMIT);
            return contract;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
            //throw new RRException("连接智能合约异常");
        }
    }

    @Bean
    //监听这里才用每次都生成一个新的对象，因为同时监听多个事件不能使用同一个实例
    @Scope("prototype")
    @Autowired
    public EthFilter ethFilter(Trace trace, Web3j  web3j) throws IOException {
        //获取启动时监听的区块
        Request<?, EthBlockNumber> request = web3j.ethBlockNumber();
        BigInteger fromblock = request.send().getBlockNumber();
        return new EthFilter(DefaultBlockParameter.valueOf(fromblock),
                //如果监听不到这里的地址可以把 0x 给去掉
                DefaultBlockParameterName.LATEST, trace.getContractAddress());
    }

}
