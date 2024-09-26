package com.github.hiwepy.dapp;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.ton.java.address.Address;
import org.ton.java.tonlib.Tonlib;
import org.ton.java.tonlib.types.*;
import org.ton.java.utils.Utils;

import java.util.Map;
import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Slf4j
public class TonlibTest {

    /**
     * Constructor, getLast, lookupBlock, getBlockHeader, getShards
     */
    @Test
    public void test1() {
        // builder
        Tonlib tonlib = Tonlib.builder()
                .pathToTonlibSharedLib("/mnt/tonlibjson.so")
                .pathToGlobalConfig("/mnt/testnet-global.config.json")
                .verbosityLevel(VerbosityLevel.FATAL)
                .testnet(true)
                .build();

        // lookupBlock
        BlockIdExt fullblock = tonlib.lookupBlock(304479,-1,-9223372036854775808L,0,0);
        log.info(fullblock.toString());

        // getLast
        MasterChainInfo masterChainInfo = tonlib.getLast();
        log.info(masterChainInfo.toString());

        // getBlockHeader
        BlockHeader header = tonlib.getBlockHeader(masterChainInfo.getLast());
        log.info(header.toString());

        // getShards
        Shards shards = tonlib.getShards(masterChainInfo.getLast());
        log.info(shards.toString());
    }

    /**
     *  Get all block transactions
     */
    @Test
    public void test2() {
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
                    log.info("{} <<<<< {} : {} ", tx.getIn_msg().getSource().getAccount_address(), tx.getIn_msg().getDestination().getAccount_address(), tx.getIn_msg().getValue());
                }
                if(Objects.nonNull(tx.getOut_msgs())){
                    for(RawMessage msg:tx.getOut_msgs()){
                        log.info("{} >>>>> {} : {} ",msg.getSource().getAccount_address(),msg.getDestination().getAccount_address(),msg.getValue());
                    }
                }
            }
        }
    }

    private static final String MY_TESTNET_VALIDATOR_ADDR = "EQCwHyzOrKP1lBHbvMrFHChifc1TLgeJVpKgHpL9sluHU-gV";

    /**
     * Get transactions by address
     */
    @Test
    public void test3() {
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

        for(RawTransaction tx:rawTransactions.getTransactions()){
            if((Objects.nonNull(tx.getIn_msg()) && (!tx.getIn_msg().getSource().getAccount_address().equals(""))){
                log.info("{}, {} <<<<< {} : {} ", Utils.toUTC(tx.getUtime()),tx.getIn_msg().getSource().getAccount_address(),tx.getIn_msg().getDestination().getAccount_address(),tx.getIn_msg().getValueToncoins(9));
            }
            if(Objects.nonNull(tx.getOut_msgs())) {
                for (RawMessage msg : tx.getOut_msgs()) {
                    log.info("{}, {} >>>>> {} : {} ", Utils.toUTC(tx.getUtime()), msg.getSource().getAccount_address(), msg.getDestination().getAccount_address(), msg.getValue());
                }
            }
        }
    }

    /**
     * Get accountHelper state, balance
     */
    @Test
    public void test4() {
        Tonlib tonlib = Tonlib.builder()
                .pathToTonlibSharedLib("/mnt/tonlibjson.so")
                .pathToGlobalConfig("/mnt/testnet-global.config.json")
                .verbosityLevel(VerbosityLevel.FATAL)
                .testnet(true)
                .build();

        Address addr = Address.of("kQBeuMOZrZyCrtvZ1dMaMKmlxpulQFCOYCLI8EYcqMvI6v6E");
        log.info("address: " + addr.toString(true));

        AccountAddressOnly accountAddressOnly=AccountAddressOnly.builder()
                .account_address(addr.toString(true))
                .build();
        FullAccountState account = tonlib.getAccountState(accountAddressOnly);

        log.info(account.toString());
        log.info("balance: {}", account.getBalance());
    }

    /**
     * Encrypt/decrypt with mnemonic
     */
    @Test
    public void test5() {
        Tonlib tonlib = Tonlib.builder()
                .pathToTonlibSharedLib("/mnt/tonlibjson.so")
                .pathToGlobalConfig("/mnt/testnet-global.config.json")
                .verbosityLevel(VerbosityLevel.FATAL)
                .testnet(true)
                .build();

        String base64mnemonic = Utils.stringToBase64("centring moist twopenny bursary could carbarn abide flirt ground shoelace songster isomeric pis strake jittery penguin gab guileful lierne salivary songbird shore verbal measures");

        String dataToEncrypt = Utils.stringToBase64("ABC");
        Data encrypted = tonlib.encrypt(dataToEncrypt,base64mnemonic);
        log.info("encrypted {}", encrypted.getBytes());

        Data decrypted = tonlib.decrypt(encrypted.getBytes(),base64mnemonic);
        String dataDecrypted = Utils.base64ToString(decrypted.getBytes());
        assertThat("ABC").isEqualTo(dataDecrypted);
    }

    /**
     * Execute run-methods：Get seqno
     */
    @Test
    public void test6_1() {
        Tonlib tonlib = Tonlib.builder()
                .pathToTonlibSharedLib("/mnt/tonlibjson.so")
                .pathToGlobalConfig("/mnt/testnet-global.config.json")
                .verbosityLevel(VerbosityLevel.FATAL)
                .testnet(true)
                .build();
        Address address = Address.of("kQB7wRCSr02IwL1nOxkEipop3goYb4oN6ZehZMS2jImwyS1t");
        RunResult result = tonlib.runMethod(address,"seqno");

        log.info("gas_used {}, exit_code {} ",result.getGas_used(),result.getExit_code());

        TvmStackEntryNumber seqno=(TvmStackEntryNumber)result.getStack().get(0);
        log.info("seqno: {}", eqno.getNumber());

        Retrieve past_election_ids


    }

    /**
     * Execute run-methods：Get seqno
     */
    @Test
    public void test6_1() {
        Tonlib tonlib = Tonlib.builder()
                .pathToTonlibSharedLib("/mnt/tonlibjson.so")
                .pathToGlobalConfig("/mnt/testnet-global.config.json")
                .verbosityLevel(VerbosityLevel.FATAL)
                .testnet(true)
                .build();
        Address address = Address.of("kQB7wRCSr02IwL1nOxkEipop3goYb4oN6ZehZMS2jImwyS1t");
        RunResult result = tonlib.runMethod(address,"seqno");

        log.info("gas_used {}, exit_code {} ",result.getGas_used(),result.getExit_code());

        TvmStackEntryNumber seqno=(TvmStackEntryNumber)result.getStack().get(0);
        log.info("seqno: {}", eqno.getNumber());

        Retrieve past_election_ids


    }
}
