package com.github.hiwepy.dapp.util;

import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint256;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static com.github.hiwepy.dapp.util.Contract.*;

public class Erc20ContractFuncUtil {

    /***********ERC20***************/
    public static Function approve(String spender, BigInteger value) {
        return new Function(
                FUNC_APPROVE,
                Arrays.asList(new Address(160, spender),
                        new Uint256(value)),
                Collections.emptyList());
    }

    /***********OPTIONS***************/
    public static Function depositFund(BigInteger amount) {
        return new Function(
                FUNC_DEPOSIT_FUND,
                Arrays.asList(new Uint256(amount)),
                Collections.emptyList());
    }

    public static Function balances(String param0) {
        return new Function(FUNC_BALANCES,
                Arrays.asList(new Address(160, param0)),
                Arrays.asList(new TypeReference<Uint256>() {
                }));
    }

    public static Function createOrder(String account, String orderId, BigInteger amount, BigInteger fee) {
        return new Function(
                FUNC_CREATE_ORDER,
                Arrays.asList(new Address(160, account),
                        HexStringUtils.stringToBytes32(orderId),
                        new Uint256(amount),
                        new Uint256(fee)),
                Collections.emptyList());
    }

    public static Function settleOrder(String orderId, BigInteger revenue, BigInteger fee) {
        return new Function(
                FUNC_SETTLE_ORDER,
                Arrays.asList(HexStringUtils.stringToBytes32(orderId),
                        new Uint256(revenue),
                        new Uint256(fee)),
                Collections.emptyList());
    }

    public static Function cumulativeIncome() {
        return new Function(FUNC_CUMULATIVE_INCOME,
                Arrays.asList(),
                Arrays.asList(new TypeReference<Uint256>() {
                }));

    }

    public static Function batchSettleOrder(List<BatchSettleOrder> orders) {
        return new Function(
                FUNC_BATCH_SETTLE_ORDERS,
                Arrays.asList(new DynamicArray<>(BatchSettleOrder.class, orders)),
                Collections.<TypeReference<?>>emptyList());
    }

    public static class BatchSettleOrder extends StaticStruct {
        public byte[] orderId;
        public BigInteger revenue;
        public BigInteger fee;

        public BatchSettleOrder(byte[] orderId, BigInteger revenue, BigInteger fee) {
            super(new Bytes32(orderId), new Uint256(revenue));
            this.orderId = orderId;
            this.revenue = revenue;
            this.fee = fee;
        }

        public BatchSettleOrder(Bytes32 orderId, Uint256 revenue, Uint256 fee) {
            super(orderId, revenue, fee);
            this.orderId = orderId.getValue();
            this.revenue = revenue.getValue();
            this.fee = fee.getValue();
        }
    }


    public static Function slot1() {
        return new Function(FUNC_SLOT1,
                Arrays.asList(),
                Arrays.asList(
                        new TypeReference<Bool>() {
                        },
                        new TypeReference<Address>() {
                        },
                        new TypeReference<Uint256>() {
                        },
                        new TypeReference<Uint256>() {
                        }));

    }

    public static Function userNumber() {
        return new Function(FUNC_USER_NUMBER,
                Arrays.asList(),
                Arrays.asList(new TypeReference<Uint256>() {
                }));
    }

    public static Function assetsManagement() {
        return new Function(FUNC_ASSETS_MANAGEMENT,
                Arrays.asList(),
                Arrays.asList(new TypeReference<Uint256>() {
                }));

    }


    public static Function slot0() {
        return new Function(FUNC_SLOT0,
                Arrays.asList(),
                Arrays.asList(new TypeReference<Address>() {
                }, new TypeReference<Address>() {
                }, new TypeReference<Address>() {
                }, new TypeReference<Address>() {
                }));
    }

    public static Function userInfo(String param0) {
        return new Function(FUNC_USER_INFO,
                Arrays.asList(new Address(160, param0)),
                Arrays.asList(new TypeReference<Uint256>() {
                }, new TypeReference<Uint256>() {
                }));
    }

    public static Function calculateShares(BigInteger shares) {
        return new Function(FUNC_CALCULATE_SHARES,
                Arrays.asList(new Uint256(shares)),
                Arrays.asList(new TypeReference<Uint256>() {
                }));
    }

    public static Function calculateAmounts(BigInteger shares) {
        return new Function(FUNC_CALCULATE_AMOUNTS,
                Arrays.asList(new Uint256(shares)),
                Arrays.asList(new TypeReference<Uint256>() {
                }));
    }

    public static Function orderInfo(String orderId) {
        return new Function(FUNC_ORDER_INFO,
                Arrays.<Type>asList(HexStringUtils.stringToBytes32(orderId)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {
                }, new TypeReference<Bool>() {
                }, new TypeReference<Bool>() {
                }, new TypeReference<Uint256>() {
                }));
    }


}
