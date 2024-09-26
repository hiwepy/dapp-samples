package com.github.hiwepy.dapp;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.ton.java.address.Address;
import org.ton.java.tonlib.Tonlib;
import org.ton.java.tonlib.types.FullAccountState;
import org.ton.java.tonlib.types.MasterChainInfo;

import java.util.ArrayDeque;
import java.util.Deque;


@Slf4j
public class TestConcurrentTonlib {


    public static final String ELECTOR_ADDRESSS = "UQAXoO7fPVzy2piMEgdcejkXCR3UrGBfNuZWcU5-e79Xk1cO"; // isWallet

    private static Tonlib tonlib = Tonlib.builder()
            .testnet(true)
            .ignoreCache(false)
//            .verbosityLevel(VerbosityLevel.DEBUG)
            .build(); // you can't use one tonlib instance for parallel queries

    @Test
    public void testTonlibRunMethod() throws InterruptedException {
        log.info("tonlib instance {}", tonlib);
        MasterChainInfo last = tonlib.getLast();
        log.info("last: {}", last);
        Thread.sleep(100);

        FullAccountState accountState = tonlib.getAccountState(Address.of("EQCwHyzOrKP1lBHbvMrFHChifc1TLgeJVpKgHpL9sluHU-gV"));
        log.info("account {}", accountState);

        Address elector = Address.of(ELECTOR_ADDRESSS);
        Deque<String> stack = new ArrayDeque<>();
        Address address = Address.of("EQCwHyzOrKP1lBHbvMrFHChifc1TLgeJVpKgHpL9sluHU-gV");
        stack.offer("[num, " + address.toDecimal() + "]");

        log.info("seqno {}", tonlib.getSeqno(address));
    }
}