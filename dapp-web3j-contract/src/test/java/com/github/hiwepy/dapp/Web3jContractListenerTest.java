package com.github.hiwepy.dapp;

import io.reactivex.disposables.Disposable;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.websocket.WebSocketService;

import java.math.BigInteger;
import java.net.ConnectException;
import java.util.concurrent.ExecutionException;

public class Web3jContractListenerTest {

    public static void main(String[] args) throws ExecutionException, InterruptedException, ConnectException {
        WebSocketService webSocketService = new WebSocketService("ws://localhost:9991", true);
        webSocketService.connect();
        Web3j client = Web3j.build(webSocketService);
        Disposable subscribe = client.ethLogFlowable(
                new EthFilter(
                        DefaultBlockParameter.valueOf(BigInteger.ONE),
                        DefaultBlockParameterName.LATEST,
                        // 合约地址
                        "0xgert5yh4trerg34t6yu3564tjyry4565u4yth4"
                )
        ).subscribe(event -> {
            System.out.println(event);
            // => Log{removed=false, logIndex='0x39', transactionIndex='0x13', transactionHash='0x9c3653c27946cb39c3a256229634cfedbca54f146a740f46b661b986590f8358', blockHash='0x1e674b9d111601519f977b75f9a1865ba359b474550ff5d0d87daec1f543d64d', blockNumber='0xb83ccd', address='0x7b52aae43e962ce6a9a1b7e79f549582ae8bcff9', data='0x', type='null', topics=[0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef, 0x0000000000000000000000000000000000000000000000000000000000000000, 0x0000000000000000000000006ab3d0bd875f8667026d2a9bb3060ec44380bcc2, 0x0000000000000000000000000000000000000000000000000000000000000017]}
        }, Throwable::printStackTrace);
    }

}
