package com.github.hiwepy.dapp;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ton.java.address.Address;
import org.ton.java.tonlib.Tonlib;
import org.ton.java.tonlib.types.*;
import org.ton.java.utils.Utils;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Objects;

/**
 * https://github.com/neodix42/ton4j/blob/main/tonlib/README.md
 */
@Slf4j
public class Web3jTransactionTest {


    Tonlib tonlib;

    @BeforeEach
    public void setUp() throws Exception {

        URL tonlibjson = Web3jTransactionTest.class.getResource("/tonlibjson.dll");
        File tonlibjsonFile = Paths.get(tonlibjson.toURI()).toFile();
        String pathToTonlibSharedLib = tonlibjsonFile.getAbsolutePath();

        URL testnetGlobalConfig = Web3jTransactionTest.class.getResource("/testnet-global.config.json");
        File testnetGlobalConfigFile = Paths.get(testnetGlobalConfig.toURI()).toFile();
        String pathToGlobalConfig = testnetGlobalConfigFile.getAbsolutePath();

        tonlib = Tonlib.builder()
                .pathToTonlibSharedLib(pathToTonlibSharedLib)
                .pathToGlobalConfig(pathToGlobalConfig)
                .verbosityLevel(VerbosityLevel.FATAL)
                .testnet(true)
                .build();
    }


    /**
     * Get all block transactions
     */
    @Test
    public void testGetAllBlockTransactionsFromTon() throws Exception {

        //lookupBlock
        BlockIdExt fullblock = tonlib.lookupBlock(444699, -1, -9223372036854775808L, 0, 0);
        log.info(fullblock.toString());

        Map<String, RawTransactions> txs = tonlib.getAllBlockTransactions(fullblock,100,null);
        for(Map.Entry<String, RawTransactions> entry: txs.entrySet()){
            for(RawTransaction tx: ((RawTransactions)entry.getValue()).getTransactions()){
                if(Objects.nonNull(tx.getIn_msg()) && (!tx.getIn_msg().getSource().getAccount_address().equals(""))){
                    RawMessage msg = tx.getIn_msg();
                    log.info("{}, {} <<<<< {} : {} ", Utils.toUTC(tx.getUtime()), msg.getSource().getAccount_address(), msg.getDestination().getAccount_address(), msg.getValue());
                }
                if(Objects.nonNull(tx.getOut_msgs())){
                    for(RawMessage msg:tx.getOut_msgs()){
                        log.info("{}, {} >>>>> {} : {} ", Utils.toUTC(tx.getUtime()), msg.getSource().getAccount_address(), msg.getDestination().getAccount_address(), msg.getValue());
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
    public void testGeTransactionsByAddressFromTon()  {

        try {

            Address address = Address.of("EQDKbjIcfM6ezt8KjKJJLshZJJSqX7XOA4ff-W72r5gqPrHF");
            log.info("address: " + address.toString(true));
            RawTransactions rawTransactions = tonlib.getRawTransactions(address.toString(false),null,null);
            log.info("total txs: {}", rawTransactions.getTransactions().size());

            for(RawTransaction tx:rawTransactions.getTransactions()) {
                if (Objects.nonNull(tx.getIn_msg()) && (!tx.getIn_msg().getSource().getAccount_address().equals(""))) {
                    RawMessage msg = tx.getIn_msg();
                    log.info("{}, {} <<<<< {} : {} ", Utils.toUTC(tx.getUtime()), msg.getSource().getAccount_address(), msg.getDestination().getAccount_address(), msg.getValue());
                }
                if (Objects.nonNull(tx.getOut_msgs())) {
                    for (RawMessage msg : tx.getOut_msgs()) {
                        log.info("{}, {} >>>>> {} : {} ", Utils.toUTC(tx.getUtime()), msg.getSource().getAccount_address(), msg.getDestination().getAccount_address(), msg.getValue());
                    }
                }
            }
        } catch (Exception e){
            log.error("error: ", e);
        }
    }

}
