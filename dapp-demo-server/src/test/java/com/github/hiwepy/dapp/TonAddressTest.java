package com.github.hiwepy.dapp;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.ton.java.address.Address;
import org.ton.java.utils.Utils;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
public class TonAddressTest {

    public static final String TEST_ADDRESS_0 = "0QAs9VlT6S776tq3unJcP5Ogsj-ELLunLXuOb1EKcOQi4-QO";
    public static final String TEST_ADDRESS_1 = "kQAs9VlT6S776tq3unJcP5Ogsj-ELLunLXuOb1EKcOQi47nL";
    public static final String TEST_ADDRESS_3 = "0:2cf55953e92efbeadab7ba725c3f93a0b23f842cbba72d7b8e6f510a70e422e3";
    public static final String TEST_ADDRESS_4 = "kf_8uRo6OBbQ97jCx2EIuKm8Wmt6Vb15-KsQHFLbKSMiYIny";

    public static final String TEST_ADDRESS_5 = "-1:cdff07eb154c2e595930a6a9a4451608251cc1c894686c4c110894de43c96ad3";

    public static final String TEST_ADDRESS_6 = "UQAXoO7fPVzy2piMEgdcejkXCR3UrGBfNuZWcU5-e79Xk1cO"; // isWallet
    public static final String TEST_ADDRESS_7 = "555"; // not wallet

    /**
     * 测试地址解析
     */
    @Test
    public void testAddressParser() {

        Address address03 = Address.of(TEST_ADDRESS_3);
        assertThat(address03.toRaw()).isEqualTo(TEST_ADDRESS_3);

        Address address04 = Address.of(TEST_ADDRESS_0);
        assertThat(address04.toString(true, true, false)).isEqualTo("0QAs9VlT6S776tq3unJcP5Ogsj-ELLunLXuOb1EKcOQi4-QO");
        assertThat(address04.isBounceable).isFalse();

        Address address05 = Address.of(TEST_ADDRESS_0);
        assertThat(address05.toString(true, true, true)).isEqualTo(TEST_ADDRESS_1);
        assertThat(address05.isBounceable).isFalse();

        Address address06 = Address.of(TEST_ADDRESS_0);
        assertThat(address06.toString(false)).isEqualTo(TEST_ADDRESS_3);
        assertThat(address06.isBounceable).isFalse();

        Address address07 = Address.of(TEST_ADDRESS_1);
        assertThat(address07.toString(true, true, false)).isEqualTo("0QAs9VlT6S776tq3unJcP5Ogsj-ELLunLXuOb1EKcOQi4-QO");
        assertThat(address07.isBounceable).isTrue();

        Address address08 = Address.of(TEST_ADDRESS_1);
        assertThat(address08.toString(true, true, true)).isEqualTo(TEST_ADDRESS_1);
        assertThat(address08.isBounceable).isTrue();

        Address address09 = Address.of(TEST_ADDRESS_1);
        assertThat(address09.toString(false)).isEqualTo(TEST_ADDRESS_3);
        assertThat(address09.isBounceable).isTrue();

        Address address10 = Address.of(TEST_ADDRESS_3);
        assertThat(address10.toString(true, true, false, true)).isEqualTo("0QAs9VlT6S776tq3unJcP5Ogsj-ELLunLXuOb1EKcOQi4-QO");

        Address address11 = Address.of(TEST_ADDRESS_3);
        assertThat(address11.toString(true, true, true, true)).isEqualTo(TEST_ADDRESS_1);

        Address address12 = Address.of(TEST_ADDRESS_3);
        assertThat(address12.toString(false)).isEqualTo(TEST_ADDRESS_3);

        Address address13 = Address.of("-1:3333333333333333333333333333333333333333333333333333333333333333");
        assertThat(address13.toString(true, false, true, false)).isEqualTo("Ef8zMzMzMzMzMzMzMzMzMzMzMzMzMzMzMzMzMzMzMzMzM0vF");
        assertThat(address13.toString(true, false, false, false)).isEqualTo("Uf8zMzMzMzMzMzMzMzMzMzMzMzMzMzMzMzMzMzMzMzMzMxYA");
        assertThat(Utils.bytesToHex(address13.hashPart)).isEqualTo("3333333333333333333333333333333333333333333333333333333333333333");
        assertThat(address13.hashPart.length).isEqualTo(32);
        assertThat(address13.wc).isEqualTo((byte) -1);

        Address address14 = Address.of(TEST_ADDRESS_4);
        assertThat(address14.isTestOnly).isTrue();

        assertThat(Address.isValid(TEST_ADDRESS_0)).isTrue();
        assertThat(Address.isValid(TEST_ADDRESS_1)).isTrue();
        assertThat(Address.isValid(TEST_ADDRESS_3)).isTrue();
        assertThat(Address.isValid(TEST_ADDRESS_4)).isTrue();
        assertThat(Address.isValid("bad_address")).isFalse();
    }

    /**
     * 测试错误的地址
     */
    @Test
    public void testBadAddress() {
        assertThrows(Exception.class, () -> {
            Address.of("");
            Address.of("bad_input");
            Address.of("kf_8uRo6OBbQ97jCx2EIuKm8Wmt6Vb15-KsQHFLbKSMiYInz");
            Address.of("ov_8uRo6OBbQ97jCx2EIuKm8Wmt6Vb15-KsQHFLbKSMiYMg3");
            Address.of("0:8uRo6OBbQ97jCx2EIuKm8Wmt6Vb15:KsQHFLbKSMiYMg3");
        });
    }

    /**
     * 测试地址比较
     */
    @Test
    public void testCompareAddress() {
        Address a = Address.of(TEST_ADDRESS_0);
        Address b = Address.of(TEST_ADDRESS_4);
        assertThat(a).isNotEqualTo(b);

        b = Address.of(TEST_ADDRESS_1);
        assertThat(a).isNotEqualTo(b);
    }

    /**
     * 使用36字节格式保存地址到文件
     * @throws IOException IO异常
     */
    @Test
    public void testSaveAddress() throws IOException {
        // wc 0
        Address address01 = Address.of(TEST_ADDRESS_0);
        log.info("full address " + address01.toString(false));
        log.info("bounceable address {}", address01.toString(true, true, true));
        log.info("non-bounceable address {}", address01.toString(true, true, false));

        address01.saveToFile("test0.addr");

        // wc -1
        Address address02 = Address.of(TEST_ADDRESS_5);
        log.info("full address {} ", address02.toString(false));
        log.info("bounceable address {}", address02.toString(true, true, true, true));
        log.info("non-bounceable address {}", address02.toString(true, true, false, true));

        address02.saveToFile("test1.addr");
    }

    /**
     * 测试地址是否是钱包地址
     */
    @Test
    public void testIsWallet() {

        // 是钱包地址
        assertThat(Address.of(TEST_ADDRESS_0).isWallet).isTrue();
        assertThat(Address.of(TEST_ADDRESS_3).isWallet).isTrue();
        assertThat(Address.of(TEST_ADDRESS_5).isWallet).isTrue();
        assertThat(Address.of(TEST_ADDRESS_6).isWallet).isTrue();
        // 不是钱包地址
        assertThat(Address.of(TEST_ADDRESS_1).isWallet).isTrue();
        assertThat(Address.of(TEST_ADDRESS_7).isWallet).isTrue();
//        assertThat(Address.of(TEST_ADDRESS_4).isWallet).isTrue();

    }
}
