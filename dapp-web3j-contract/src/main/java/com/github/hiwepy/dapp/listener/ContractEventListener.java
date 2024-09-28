package com.github.hiwepy.dapp.listener;

import com.google.gson.Gson;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthLog;
import org.web3j.protocol.websocket.WebSocketService;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class ContractEventListener {
    // 合约地址，如0x0000000000000000000000000000000000000000
    private final String contractAddress;
    // 节点的websocket-url，如：ws://localhost:9991
    private final String ethereumRpcUrl;

    private WebSocketService ws;
    private Web3j web3j;
    private EthFilter filter;
    private AtomicInteger finalLogSize = new AtomicInteger(0);
    private ExecutorService threadPool = Executors.newSingleThreadExecutor();

    @Autowired
    public ContractEventListener(
        @Value("${contract.address}") String contractAddress,
        @Value("${contract.websocket-url}") String ethereumRpcUrl
    ) {
        this.contractAddress = contractAddress;
        this.ethereumRpcUrl = ethereumRpcUrl;
    }

    @PostConstruct
    public void listenForAllEvents() {
        try {
            //初始化WebSocket链接
            initializeWebSocket();
            //日志打印filter地址，验证filter是否有效
            log.error("filter-getAddress:" + filter.getAddress() + ", getFromBlock:" + filter.getFromBlock().getValue());
            //ethGetLogs 主动获取合约的事件日志
            List<EthLog.LogResult> logResultList = web3j.ethGetLogs(filter).send().getLogs();
            log.error("ethGetLogs size: " + logResultList.size());
            //单独开一个线程来执行，防止卡住影响主线程
            Runnable subscriptionTask = () -> {
                try {
                    //订阅合约事件方法
                    subscribeLog(web3j, filter, finalLogSize);
                } catch (Exception e) {
                    log.error("subscribe error: " + e.getMessage());
                    scheduleWebSocketReconnectCheck();
                }
            };
            threadPool.submit(subscriptionTask);
            //防止节点断开链接后，后续无法获取事件，从而定时校验并链接
            scheduleWebSocketReconnectCheck();
        } catch (Exception e) {
            log.error("thread pool execution error: " + e.getMessage());
            scheduleWebSocketReconnectCheck();
        }
    }

    private void initializeWebSocket() throws Exception {
        ws = new WebSocketService(ethereumRpcUrl, true);
        ws.connect();
        web3j = Web3j.build(ws);
        //filter 事件过滤器，初始化从第一个块到最后一个块的contractAddress合约的所有事件
        filter = new EthFilter(DefaultBlockParameter.valueOf(BigInteger.ONE),
                DefaultBlockParameterName.LATEST, new ArrayList<>(Collections.singleton(contractAddress)));
    }

    private void scheduleWebSocketReconnectCheck() {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        //定时校验链接
        scheduler.scheduleAtFixedRate(() -> {
            try {
                if (!isWebSocketConnected()) {
                    log.warn("WebSocket connection is not active. Reconnecting...");
                    ws.close();
                    initializeWebSocket();
                }

            } catch (Exception e) {
                log.error("WebSocket reconnect error: " + e.getMessage());
                try {
                    initializeWebSocket();
                } catch (Exception ex) {
                    log.error("WebSocket reconnect RuntimeException: " + e.getMessage());
                    throw new RuntimeException(ex);
                }
            }
        }, 0, 30, TimeUnit.SECONDS);
    }

    private boolean isWebSocketConnected() {
        //Java Web3J未找到校验链接的方法，因此通过调用方法来实现
        log.error("isWebSocketConnected ");
        try {
            web3j.ethGetLogs(filter).send().getLogs();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private void subscribeLog(Web3j web3jWs, EthFilter filter, AtomicInteger finalLogSize) {
        //具体的订阅事件方法web3jWs.ethLogFlowable(filter).subscribe
        web3jWs.ethLogFlowable(filter).subscribe(event -> {
            log.info("subscribe-event: " + event);
            Gson gson = new Gson();
            List<EthLog.LogResult> logResultList = web3jWs.ethGetLogs(filter).send().getLogs();
            log.info("subscribe-event-size :" + logResultList.size());
            if (finalLogSize.get() < logResultList.size()) {
                String logJson = gson.toJson(event);
                LogObject logObject = gson.fromJson(logJson, LogObject.class);
                String dataHex = logObject.getData().substring(2);  // Remove the '0x' prefix
                byte[] dataBytes = Numeric.hexStringToByteArray(dataHex);
                String decodedData = new String(dataBytes);

                log.info("subscribe - Decoded Data: " + decodedData);
                Proposal proposal = gson.fromJson(decodedData, Proposal.class);
                EthBlock.Block block = web3jWs.ethGetBlockByHash(logObject.getBlockHash(), false).send().getBlock();
                String hexTimestamp = String.valueOf(block.getTimestamp());
                long milliseconds = Long.parseLong(hexTimestamp) * 1000;
                finalLogSize.set(finalLogSize.get() + 1);
            }
        }, Throwable::printStackTrace);
    }

    //事件数据结构
    class LogObject {
        private boolean removed;
        private String logIndex;
        private String transactionIndex;
        private String transactionHash;
        private String blockHash;
        private String blockNumber;
        private String address;
        private String data;
        private String type;
        private String[] topics;

        public boolean isRemoved() {
            return removed;
        }

        public void setRemoved(boolean removed) {
            this.removed = removed;
        }

        public String getLogIndex() {
            return logIndex;
        }

        public void setLogIndex(String logIndex) {
            this.logIndex = logIndex;
        }

        public String getTransactionIndex() {
            return transactionIndex;
        }

        public void setTransactionIndex(String transactionIndex) {
            this.transactionIndex = transactionIndex;
        }

        public String getTransactionHash() {
            return transactionHash;
        }

        public void setTransactionHash(String transactionHash) {
            this.transactionHash = transactionHash;
        }

        public String getBlockHash() {
            return blockHash;
        }

        public void setBlockHash(String blockHash) {
            this.blockHash = blockHash;
        }

        public String getBlockNumber() {
            return blockNumber;
        }

        public void setBlockNumber(String blockNumber) {
            this.blockNumber = blockNumber;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String[] getTopics() {
            return topics;
        }

        public void setTopics(String[] topics) {
            this.topics = topics;
        }
    }

    //事件中data的具体数据格式
    class Proposal {
        private long ID;

        private int Type;

        private int Strategy;

        private String Proposer;

        private String Title;

        private String Desc;

        private int BlockNumber;

        private int TotalVotesl;

        private String[] PassVotes;

        private String[] RejectVotes;

        private int Status;

        private List<Candidate> Candidates;

        public long getID() {
            return ID;
        }

        public void setID(long ID) {
            this.ID = ID;
        }

        public int getType() {
            return Type;
        }

        public void setType(int type) {
            Type = type;
        }

        public int getStrategy() {
            return Strategy;
        }

        public void setStrategy(int strategy) {
            Strategy = strategy;
        }

        public String getProposer() {
            return Proposer;
        }

        public void setProposer(String proposer) {
            Proposer = proposer;
        }

        public String getTitle() {
            return Title;
        }

        public void setTitle(String title) {
            Title = title;
        }

        public String getDesc() {
            return Desc;
        }

        public void setDesc(String desc) {
            Desc = desc;
        }

        public long getBlockNumber() {
            return BlockNumber;
        }

        public void setBlockNumber(int blockNumber) {
            BlockNumber = blockNumber;
        }

        public int getTotalVotesl() {
            return TotalVotesl;
        }

        public void setTotalVotesl(int totalVotesl) {
            TotalVotesl = totalVotesl;
        }

        public String[] getPassVotes() {
            return PassVotes;
        }

        public void setPassVotes(String[] passVotes) {
            PassVotes = passVotes;
        }

        public String[] getRejectVotes() {
            return RejectVotes;
        }

        public void setRejectVotes(String[] rejectVotes) {
            RejectVotes = rejectVotes;
        }

        public int getStatus() {
            return Status;
        }

        public void setStatus(int status) {
            Status = status;
        }

        public List<Candidate> getCandidates() {
            return Candidates;
        }

        public void setCandidates(List<Candidate> candidates) {
            Candidates = candidates;
        }
    }

    public class Candidate {
        private String Address;
        private String Weight;
        private String Name;


        public String getAddress() {
            return Address;
        }

        public void setAddress(String address) {
            Address = address;
        }

        public String getWeight() {
            return Weight;
        }

        public void setWeight(String weight) {
            Weight = weight;
        }

        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
        }
    }
}

