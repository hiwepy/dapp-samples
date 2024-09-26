package com.github.hiwepy.dapp.util;

import com.alibaba.fastjson2.JSON;
import com.github.hiwepy.dapp.enums.ChainEnum;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.utils.Convert;
import org.web3j.utils.Convert.Unit;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Slf4j
public class Erc20ContractUtil {
    public static BigDecimal getBalance(Web3j web3j, String address) {
        try {
            EthGetBalance ethGetBalance = web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST).send();
            if (ethGetBalance.hasError()) {
                return BigDecimal.valueOf(-1);
            }
            return Convert.fromWei(new BigDecimal(ethGetBalance.getBalance()), Unit.ETHER);
        } catch (Exception e) {
            log.error("ethGetBalance getPlatformTokenBalance exception", e);
            return BigDecimal.valueOf(-1);
        }
    }


    public static BigInteger getTokenBalanceOf(Web3j web3j, String funcName, String eoaAddress, String tokenAddress) {
        Function func = new Function(
                funcName,
                Collections.singletonList(new Address(eoaAddress)),
                Collections.singletonList(new TypeReference<Uint256>() {
                }));
        List<Type> result;
        try {
            result = ethCall(web3j, null, tokenAddress, func);
            if (!CollectionUtils.isEmpty(result)) {
                return (BigInteger) result.get(0).getValue();
            }
        } catch (Exception e) {
            return BigInteger.valueOf(-1);
        }
        return BigInteger.valueOf(-1);
    }


    public static BigInteger getTokenDecimals(Web3j web3j, String funcName, String tokenAddress) {
        Function func = new Function(
                funcName,
                Collections.emptyList(),
                Collections.singletonList(new TypeReference<Uint256>() {
                }));
        List<Type> result;
        try {
            result = ethCall(web3j, null, tokenAddress, func);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (CollectionUtils.isEmpty(result)) {
            return BigInteger.valueOf(-1);
        } else {
            return ((BigInteger) result.get(0).getValue());
        }
    }

    /**
     * ethCall
     *
     * @param web3j
     * @param from
     * @param contract
     * @param function
     * @return
     */
    public static List<Type> ethCall(Web3j web3j, String from, String contract, Function function) {
        // 对合约函数进行编码
        String encodeData = FunctionEncoder.encode(function);
        // 创建交易
        Transaction transaction = Transaction.createEthCallTransaction(from, contract, encodeData);
        try {
            // 发送交易
            EthCall response = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).send();
            // 解析返回值
            if (Objects.nonNull(response) && Objects.nonNull(response.getValue())) {
                return FunctionReturnDecoder.decode(response.getValue(), function.getOutputParameters());
            }
            log.error("ethCall error ,from:{}，contract:{},function:{},params:{},response:{}", from
                    , contract, function.getName(), JSON.toJSONString(function.getInputParameters()),
                    JSON.toJSONString(response));

        } catch (Exception e) {
            log.error("ethCall Exception", e);
            throw new RuntimeException("ethCall Exception", e);
        }
        throw new RuntimeException("ethCall error");
    }

    /**
     * ethEstimateGas
     *
     * @param web3j
     * @param from
     * @param contract
     * @param function
     * @return
     */
    public static BigInteger getTransactionGasLimit(Web3j web3j, String from, String contract, Function function) {
        String encodeData = FunctionEncoder.encode(function);
        Transaction transaction = Transaction.createEthCallTransaction(from, contract, encodeData);
        try {
            EthEstimateGas ethEstimateGas = web3j.ethEstimateGas(transaction).send();
            if (ethEstimateGas.hasError()) {
                log.error("ethEstimateGas getTransactionGasLimit error,{}，data:{},contract:{}", ethEstimateGas.getError().getMessage(),encodeData,contract);
                throw new RuntimeException(ethEstimateGas.getError().getMessage());
            }
            return new BigDecimal(ethEstimateGas.getAmountUsed()).multiply(new BigDecimal("1.3")).toBigInteger();
        } catch (IOException e) {
            log.error("ethEstimateGas getTransactionGasLimit exception", e);
            throw new RuntimeException("ethEstimateGas getTransactionGasLimit exception", e);
        }
    }

    /**
     * 获取账户的Nonce
     * @param address
     * @return
     */
    public static BigInteger getNonce(Web3j web3j, String address) {
        try {
            EthGetTransactionCount getNonce = web3j.ethGetTransactionCount(address, DefaultBlockParameterName.PENDING).send();
            if (Objects.isNull(getNonce)) {
                return new BigInteger("-1");
            }
            return getNonce.getTransactionCount();
        } catch (Exception e) {
            return new BigInteger("-1");
        }
    }

    public static BigInteger getGasPrice(Web3j web3j, BigDecimal gasPrice) {
        try {
            EthGasPrice result = web3j.ethGasPrice().send();
            if (Objects.nonNull(result)
                    && Objects.nonNull(result.getGasPrice())
                    && gasPrice.compareTo(new BigDecimal("-1")) != 0) {
                BigDecimal gasPriceCurrent = new BigDecimal(result.getGasPrice());
                BigDecimal defaultValue = Convert.toWei(gasPrice, Unit.GWEI);
                gasPrice = gasPriceCurrent.compareTo(defaultValue) > 0 ? gasPriceCurrent : defaultValue;
            }
        } catch (Exception e) {
            log.warn("getGasPrice exception");
        }
        return gasPrice.toBigInteger();
    }

    public static EthLog getEthLogs(Web3j web3j, EthFilter ethFilter, Integer chainId,
                                    String eventName) {
        EthLog logs;
        try {
            logs = web3j.ethGetLogs(ethFilter).send();
            if (logs.hasError()) {
                throw new RuntimeException("ethGetLogs send has error:" + chainId + "  " + eventName);
            }
        } catch (IOException e) {
            log.warn("ethGetLogs send exception:" + chainId + "  " + eventName + " ", e);
            throw new RuntimeException("ethGetLogs send exception:" + chainId + "  " + eventName);
        }
        return logs;
    }

    public static Long getTxTime(Web3j web3j, BigInteger blockNumber) {
        return getEthBlock(web3j, blockNumber).getBlock().getTimestamp().longValue();
    }


    @SneakyThrows
    public static EthBlock getEthBlock(Web3j web3j, BigInteger blockNumber) {
        return web3j.ethGetBlockByNumber(DefaultBlockParameter.valueOf(blockNumber), false).send();
    }


    public static Boolean transactionCheck(Web3j web3j, String hash) {
        Optional<TransactionReceipt> receipt;
        try {
            receipt = web3j.ethGetTransactionReceipt(hash).send().getTransactionReceipt();
            if (receipt.isPresent()) {
                TransactionReceipt transactionReceipt = receipt.get();
                return transactionReceipt.isStatusOK();
            }
        } catch (Exception e) {
            log.warn("transactionCheck exception");
        }
        return null;
    }


    public static String sendRawTransactionCredentials(Web3j web3j, long chainId, BigInteger nonce, BigInteger gasLimit, Credentials credentials, Function function, String contract) {
        boolean eip155 = ChainEnum.isEIP155(chainId);
        if (eip155) {
            return sendRawTransactionEIP155(web3j, chainId, nonce, gasLimit, credentials, function, contract);
        } else {
            return sendRawTransaction(web3j, chainId, nonce, gasLimit, credentials, function, contract);
        }
    }


    public static String sendRawTransaction(Web3j web3j, long chainId, BigInteger nonce, BigInteger gasLimit, Credentials credentials, Function function, String contract) {
        String functionEncode = FunctionEncoder.encode(function);
        try {
            // 获取当前区块的baseFee
            EthBlock latestBlock = web3j.ethGetBlockByNumber(DefaultBlockParameterName.LATEST, true).send();
            BigInteger baseFee;
            try {
                baseFee = latestBlock.getBlock().getBaseFeePerGas() != null ? latestBlock.getBlock().getBaseFeePerGas() : BigInteger.ZERO;
            } catch (Exception e) {
                baseFee = BigInteger.ZERO;
            }

            // 动态设置maxPriorityFeePerGas基于网络条件
            BigInteger recommendedMaxPriorityFeePerGas = web3j.ethGasPrice().send().getGasPrice();
            BigInteger userSetPriorityFee = Convert.toWei("2", Unit.GWEI).toBigInteger();
            BigInteger maxPriorityFeePerGas = recommendedMaxPriorityFeePerGas.max(userSetPriorityFee); // 取推荐值和用户设置值中的较大者

            // 设置maxFeePerGas为baseFee的2倍加上maxPriorityFeePerGas，以应对波动
            BigInteger maxFeePerGas = baseFee.multiply(BigInteger.valueOf(2)).add(maxPriorityFeePerGas);

            // 创建交易，包括chainId以支持EIP-155
            RawTransaction rawTransaction = RawTransaction.createTransaction(
                    chainId, nonce, gasLimit, contract, BigInteger.ZERO, functionEncode, maxPriorityFeePerGas, maxFeePerGas);

            EthSendTransaction response =
                    web3j.ethSendRawTransaction(Numeric.toHexString(TransactionEncoder.signMessage(rawTransaction, chainId, credentials)))
                            .sendAsync()
                            .get();
            if (response.hasError()) {
                log.warn("Error Erc20ContractUtil.sendRawTransactionCredentials ethSendRawTransaction,message:{}", response.getError().getMessage());
                return null;
            } else {
                return response.getTransactionHash();

            }
        } catch (Exception e) {
            log.warn("Exception Erc20ContractUtil.sendRawTransactionCredentials", e);
            return null;
        }
    }


    public static String sendRawTransactionEIP155(Web3j web3j, long chainId, BigInteger nonce, BigInteger gasLimit, Credentials credentials, Function function, String contract) {
        String functionEncode = FunctionEncoder.encode(function);
        try {
            // 动态获取网络的推荐 Gas Price
            BigInteger recommendedGasPrice = web3j.ethGasPrice().send().getGasPrice();
            BigInteger margin = recommendedGasPrice.multiply(BigInteger.valueOf(120)).divide(BigInteger.valueOf(100));
            // 确定交易使用的最终 Gas Price
            BigInteger finalGasPrice = recommendedGasPrice.max(margin);

            // 创建交易
            RawTransaction rawTransaction = RawTransaction.createTransaction(
                    nonce,
                    finalGasPrice,
                    gasLimit,
                    contract,
                    BigInteger.ZERO,
                    functionEncode
            );

            EthSendTransaction response =
                    web3j.ethSendRawTransaction(Numeric.toHexString(TransactionEncoder.signMessage(rawTransaction, chainId, credentials)))
                            .sendAsync()
                            .get();
            if (response.hasError()) {
                log.warn("Error Erc20ContractUtil.sendRawTransactionCredentials ethSendRawTransaction,message:{}", response.getError().getMessage());
                return null;
            } else {
                return response.getTransactionHash();

            }
        } catch (Exception e) {
            log.warn("Exception Erc20ContractUtil.sendRawTransactionCredentials", e);
            return null;
        }
    }

}
