package com.github.hiwepy.dapp;

import com.blockchain.tools.eth.codec.EthAbiCodecTool;
import com.blockchain.tools.eth.contract.template.ERC20Contract;
import com.blockchain.tools.eth.contract.util.EthContractUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import java.math.BigInteger;
import java.util.List;

@Slf4j
public class TonConnectTest {

    private String senderAddress = ""; // 发送者地址
    private String toAddress = ""; // 接收者地址

    /**
     * 合约查询 以及 写入数据
     * @throws Exception
     */
    @Test
    public void testTonConnect3() throws Exception {
        String privateKey = ""; // 私钥
        Web3j web3j = Web3j.build(new HttpService("https://data-seed-prebsc-1-s1.binance.org:8545/")); // 链的RPC地址
        String contractAddress = "";

        EthContractUtil ethContractUtil = EthContractUtil.builder(web3j);

// 查询
        List<Type> result = ethContractUtil.select(
                contractAddress, // 合约地址
                EthAbiCodecTool.getInputData(
                        "balanceOf", // 要调用的方法名称
                        new Address(toAddress) // 方法的参数，如果有多个，可以继续传入下一个参数
                ),  // 要调用的方法的inputData
                new TypeReference<Uint256>() {} // 方法的返回类型，如果有多个返回值，可以继续传入下一个参数
        );
        /*
        // 往合约里写入数据
        // gasPrice，gasLimit 两个参数，如果想用默认值可以不传，或者传null
        // 如果不传的话，两个参数都必须不传，要传就一起传， 如果设置为null的话，可以一个为null，一个有值
        SendResultModel sendResultModel = ethContractUtil.sendRawTransaction(
                senderAddress, // 调用者的地址
                contractAddress, // 合约地址
                privateKey, // senderAddress的私钥
                new BigInteger("1200000"), // gasPrice，如果想用默认值 可以直接传null，或者不传这个参数
                new BigInteger("800000"), // gasLimit，如果想用默认值 可以直接传null，或者不传这个参数
                EthAbiCodecTool.getInputData(
                        "transfer", // 要调用的方法名称
                        new Address(toAddress), // 方法的参数，如果有多个，可以继续传入下一个参数
                        new Uint256(new BigInteger("1000000000000000000")) // 方法的参数，如果有多个，可以继续传入下一个参数
                ) // 要调用的方法的inputData
        );

        sendResultModel.getEthSendTransaction(); // 发送交易后的结果
        sendResultModel.getEthGetTransactionReceipt(); // 交易成功上链后的结果*/
    }

    /**
     * 调用ERC20合约
     */
    @Test
    public void testTonConnect() throws Exception {
        Web3j web3j = Web3j.build(new HttpService("https://data-seed-prebsc-2-s1.binance.org:8545"));

        String contractAddress = "";

        ERC20Contract erc20Contract = ERC20Contract.builder(web3j, contractAddress);


// 调用合约的 totalSupply 函数
        BigInteger total = erc20Contract.totalSupply();

// 调用合约的 balanceOf 函数
        BigInteger amount = erc20Contract.balanceOf("0xb4e32492E9725c3215F1662Cf28Db1862ed1EE84");

// 调用合约的 allowance 函数
        BigInteger amoun3t = erc20Contract.allowance("0xb4e32492E9725c3215F1662Cf28Db1862ed1EE84", "0x552115849813d334C58f2757037F68E2963C4c5e");
    }

}
