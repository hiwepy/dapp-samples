package com.github.hiwepy.dapp.util;


import com.github.hiwepy.api.util.OkHttpClientCreater;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;
import org.ton.java.address.Address;
import org.ton.java.tonlib.Tonlib;
import org.ton.java.tonlib.types.RawMessage;
import org.ton.java.tonlib.types.RawTransaction;
import org.ton.java.tonlib.types.RawTransactions;
import org.ton.java.tonlib.types.VerbosityLevel;
import org.ton.java.utils.Utils;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Slf4j
public class Ton4jSdkUtil {

    protected final static String APPLICATION_JSON_VALUE = "application/json";
    protected static OkHttpClient httpClient = OkHttpClientCreater.createOKHttpClient(HttpLoggingInterceptor.Level.BODY);

    /**
     * toncenter主网的API方法
      */
    protected static String TON_HTTP_API_HOST = "https://toncenter.com";
    /**
     * toncenter测试网的API方法
     */
    protected static String TON_HTTP_API_TEST_HOST = "https://testnet.toncenter.com";
    protected static String TON_API_KEY = "••••••";


    /**
     * 检查交易是否由指定地址发起
     * @param hashAddress 交易hash地址
     * @param address 发起交易的钱包地址
     * @return true: 交易由指定地址发起；false: 交易不是由指定地址发起
     */
    public static boolean checkTransaction(String hashAddress, String address) {

        // TODO 待调试通

        Tonlib tonlib = Tonlib.builder()
                .pathToTonlibSharedLib("/mnt/tonlibjson.so")
                .pathToGlobalConfig("/mnt/testnet-global.config.json")
                .verbosityLevel(VerbosityLevel.FATAL)
                .testnet(true)
                .build();

        Address addressObj = Address.of(hashAddress.getBytes(StandardCharsets.UTF_8));
        log.info("address: " + addressObj.toString(true));
        RawTransactions rawTransactions = tonlib.getRawTransactions(addressObj.toString(false),null,null);
        log.info("total txs: {}", rawTransactions.getTransactions().size());

        for(RawTransaction tx:rawTransactions.getTransactions()) {
            if (Objects.nonNull(tx.getIn_msg()) && (!tx.getIn_msg().getSource().getAccount_address().equals(address))) {
                log.info("{}, {} <<<<< {} : {} ", Utils.toUTC(tx.getUtime()), tx.getIn_msg().getSource().getAccount_address(), tx.getIn_msg().getDestination().getAccount_address(), tx.getIn_msg().getValue());
                return false;
            }
            if (Objects.nonNull(tx.getOut_msgs())) {
                for (RawMessage msg : tx.getOut_msgs()) {
                    log.info("{}, {} >>>>> {} : {} ", Utils.toUTC(tx.getUtime()), msg.getSource().getAccount_address(), msg.getDestination().getAccount_address(), msg.getValue());
                }
            }
        }
        return true;
    }

}
