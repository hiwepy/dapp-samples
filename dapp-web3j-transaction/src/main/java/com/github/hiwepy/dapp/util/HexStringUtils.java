package com.github.hiwepy.dapp.util;


import org.apache.commons.codec.binary.StringUtils;
import org.web3j.abi.datatypes.generated.Bytes32;

public class HexStringUtils {

    public static Bytes32 stringToBytes32(String string) {
        byte[] bytesValue = string.getBytes();
        byte[] bytesValueLen32 = new byte[32];
        System.arraycopy(bytesValue, 0, bytesValueLen32, 0, bytesValue.length);
        return new Bytes32(bytesValueLen32);

    }

    public static String bytes32ToSting(byte[] value) {
        return StringUtils.newStringUsAscii(value);
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes) {
            String hex = Integer.toHexString(aByte & 0xFF);
            if (hex.length() < 2) {
                sb.append(0);
            } else {
                sb.append(hex);
            }
        }
        return sb.toString();
    }


}
