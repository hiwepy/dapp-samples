package com.github.hiwepy.dapp;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.ton.java.address.Address;
import org.ton.java.tonlib.Tonlib;
import org.ton.java.tonlib.types.*;
import org.ton.java.utils.Utils;

import java.util.Map;
import java.util.Objects;

@Slf4j
public class TonTransactionTest {


    private static final byte[] MY_TESTNET_VALIDATOR_ADDR = null ;

    /**
     * Get all block transactions
     */
    @Test
    public void testTonConnect() throws Exception {
        Tonlib tonlib = Tonlib.builder()
                .pathToTonlibSharedLib("/mnt/tonlibjson.so")
                .pathToGlobalConfig("/mnt/testnet-global.config.json")
                .verbosityLevel(VerbosityLevel.FATAL)
                .testnet(true)
                .build();

        //lookupBlock
        BlockIdExt fullblock = tonlib.lookupBlock(444699, -1, -9223372036854775808L, 0, 0);
        log.info(fullblock.toString());

        Map<String, RawTransactions> txs = tonlib.getAllBlockTransactions(fullblock,100,null);
        for(Map.Entry<String, RawTransactions> entry: txs.entrySet()){
            for(RawTransaction tx: ((RawTransactions)entry.getValue()).getTransactions()){
                if(Objects.nonNull(tx.getIn_msg()) && (!tx.getIn_msg().getSource().getAccount_address().equals(""))){
                    log.info("{} <<<<< {} : {} ", tx.getIn_msg().getSource().getAccount_address(), tx.getIn_msg().getDestination().getAccount_address(),tx.getIn_msg().getValueToncoins(9));
                }
                if(Objects.nonNull(tx.getOut_msgs()){
                    for(RawMessage msg:tx.getOut_msgs()){
                        log.info("{} >>>>> {} : {} ",msg.getSource().getAccount_address(),msg.getDestination().getAccount_address(),msg.getValue());
                    }
                }
            }
        }

    }

    /**
     * Get transactions by address
     * @throws Exception
     */
    @Test
    public void testTonConnectExample() throws Exception {

        Tonlib tonlib = Tonlib.builder()
                .pathToTonlibSharedLib("/mnt/tonlibjson.so")
                .pathToGlobalConfig("/mnt/testnet-global.config.json")
                .verbosityLevel(VerbosityLevel.FATAL)
                .testnet(true)
                .build();

        Address address = Address.of(MY_TESTNET_VALIDATOR_ADDR);
        log.info("address: " + address.toString(true));
        RawTransactions rawTransactions = tonlib.getRawTransactions(address.toString(false),null,null);
        log.info("total txs: {}", rawTransactions.getTransactions().size());

        for(RawTransaction tx:rawTransactions.getTransactions()) {
            if (Objects.nonNull(tx.getIn_msg()) && (!tx.getIn_msg().getSource().getAccount_address().equals(""))) {
                log.info("{}, {} <<<<< {} : {} ", Utils.toUTC(tx.getUtime()), tx.getIn_msg().getSource().getAccount_address(), tx.getIn_msg().getDestination().getAccount_address(), tx.getIn_msg().getValueToncoins(9));
            }
            if (Objects.nonNull(tx.getOut_msgs())) {
                for (RawMessage msg : tx.getOut_msgs()) {
                    log.info("{}, {} >>>>> {} : {} ", Utils.toUTC(tx.getUtime()), msg.getSource().getAccount_address(), msg.getDestination().getAccount_address(), msg.getValue());
                }
            }
        }
    }

}
