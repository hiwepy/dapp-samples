package com.github.hiwepy.dapp;

import com.blockchain.scanning.biz.thread.model.EventModel;
import com.blockchain.scanning.chain.model.TransactionModel;
import com.blockchain.scanning.chain.model.eth.EthTransactionModel;
import com.blockchain.scanning.commons.enums.BlockEnums;
import lombok.extern.slf4j.Slf4j;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.http.HttpService;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
@Slf4j
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
        BigInteger beginBlockNumber = new BigInteger("-1");
        try {

            BigInteger lastBlockNumber = web3j.ethBlockNumber().send().getBlockNumber();

            if (beginBlockNumber.compareTo(BlockEnums.LAST_BLOCK_NUMBER.getValue()) == 0) {
                beginBlockNumber = lastBlockNumber;
            }

            if (beginBlockNumber.compareTo(lastBlockNumber) > 0) {
                log.info("[ETH], The block height on the chain has fallen behind the block scanning progress, pause scanning in progress ...... , scan progress [{}], latest block height on chain:[{}]", beginBlockNumber, lastBlockNumber);
                return;
            }

            /*if(blockChainConfig.getEndBlockNumber().compareTo(BigInteger.ZERO) > 0
                    && beginBlockNumber.compareTo(blockChainConfig.getEndBlockNumber()) >= 0){
                logger.info("[ETH], The current block height has reached the stop block height you set, so the scan job has been automatically stopped , scan progress [{}], end block height on chain:[{}]", beginBlockNumber, blockChainConfig.getEndBlockNumber());
                scanService.shutdown();
                return;
            }*/

            EthBlock block = web3j.ethGetBlockByNumber(DefaultBlockParameter.valueOf(beginBlockNumber), true).send();
            if (block == null || block.getBlock() == null) {
                log.info("[ETH], Block height [{}] does not exist", beginBlockNumber);
                if (lastBlockNumber.compareTo(beginBlockNumber) > 0) {
                    //blockChainConfig.setBeginBlockNumber(beginBlockNumber.add(BigInteger.ONE));

                    //If the block is skipped, the retry strategy needs to be notified
                    //addRetry(beginBlockNumber);
                }
                //scanService.setCurrentBlockHeight(beginBlockNumber);
                return;
            }

            List<EthBlock.TransactionResult> transactionResultList = block.getBlock().getTransactions();
            if (transactionResultList == null || transactionResultList.size() < 1) {
                log.info("[ETH], No transactions were scanned on block height [{}]", beginBlockNumber);
                if (lastBlockNumber.compareTo(beginBlockNumber) > 0) {
                   // blockChainConfig.setBeginBlockNumber(beginBlockNumber.add(BigInteger.ONE));

                    //If the block is skipped, the retry strategy needs to be notified
                    //addRetry(beginBlockNumber);
                }
               // scanService.setCurrentBlockHeight(beginBlockNumber);
                return;
            }

            List<TransactionModel> transactionList = new ArrayList<>();

            for (EthBlock.TransactionResult<EthBlock.TransactionObject> transactionResult : transactionResultList) {

                EthBlock.TransactionObject transactionObject = transactionResult.get();

                transactionList.add(
                        TransactionModel.builder().setEthTransactionModel(
                                EthTransactionModel.builder()
                                        .setEthBlock(block)
                                        .setTransactionObject(transactionObject)
                        )
                );
            }

            /*eventQueue.add(EventModel.builder()
                    .setCurrentBlockHeight(beginBlockNumber)
                    .setTransactionModels(transactionList)
            );

            blockChainConfig.setBeginBlockNumber(beginBlockNumber.add(BigInteger.ONE));

            scanService.setCurrentBlockHeight(beginBlockNumber);*/
        } catch (Exception e) {
            log.error("[ETH], An exception occurred while scanning, block height:[{}]", beginBlockNumber, e);
        }
    }
}
