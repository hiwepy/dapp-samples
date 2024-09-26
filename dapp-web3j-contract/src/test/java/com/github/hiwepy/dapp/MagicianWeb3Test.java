package com.github.hiwepy.dapp;

import com.blockchain.web3.MagicianWeb3;
import com.blockchain.web3.eth.codec.EthAbiCodec;
import com.blockchain.web3.eth.contract.EthContract;
import com.blockchain.web3.eth.contract.model.SendResultModel;
import org.junit.jupiter.api.Test;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import java.math.BigInteger;
import java.util.List;

public class MagicianWeb3Test {


    /**
     * InputData 编解码
     * @throws Exception
     */
    @Test
    public void testInputData() throws Exception {
        EthAbiCodec ethAbiCodec = MagicianWeb3.getEthBuilder().getEthAbiCodec();

        // 编码
        String inputData = ethAbiCodec.getInputData(
                "transfer", // 方法名
                new Address(toAddress), // 参数 1
                new Uint256(new BigInteger("1000000000000000000")) // 参数 2 ，如果还有其他参数，可以继续传入下一个
        );

        // 解码
        List<Type> result = ethAbiCodec.decoderInputData(
                "0x" + inputData.substring(10), // 去除方法签名的 inputData
                new TypeReference<Address>() {}, // 被编码的方法的参数 1 类型
                new TypeReference<Uint256>() {} // 被编码的方法的参数 2 类型， 如果还有其他参数，可以继续传入下一个
        );

        for(Type type : result){
            System.out.println(type.getValue());
        }

        // 获取方法签名，其实就是 inputData 的前十位
        String functionCode = ethAbiCodec.getFunAbiCode(
                "transfer", // 方法名
                new Address(toAddress), // 参数 1 ，值随意传，反正我们要的方法签名，不是完整的 inputData
                new Uint256(new BigInteger("1000000000000000000")) // 参数 2 ，值随意传，反正我们要的方法签名，不是完整的 inputData ，如果还有其他参数，可以继续传入下一个
        );


    }

    private String fromAddressPrivateKey = ""; // 发送者地址的私钥

    private String fromAddress = ""; // 发送者地址
    private String toAddress = ""; // 接收者地址
    private String  contractAddress = ""; // 合约地址

    /**
     * 合约查询 以及 写入数据
     * @throws Exception
     */
    @Test
    public void testInputData3() throws Exception {
        String privateKey = ""; // 私钥
        Web3j web3j = Web3j.build(new HttpService("https://data-seed-prebsc-1-s1.binance.org:8545/")); // 链的 RPC 地址

        EthContract ethContract = MagicianWeb3.getEthBuilder().getEthContract(web3j, fromAddressPrivateKey);
        EthAbiCodec ethAbiCodec = MagicianWeb3.getEthBuilder().getEthAbiCodec();

// 查询
        List<Type> result = ethContract.select(
                contractAddress, // 合约地址
                ethAbiCodec.getInputData(
                        "balanceOf", // 要调用的方法名称
                        new Address(toAddress) // 方法的参数，如果有多个，可以继续传入下一个参数
                ),  // 要调用的方法的 inputData
                new TypeReference<Uint256>() {} // 方法的返回类型，如果有多个返回值，可以继续传入下一个参数
        );

// 往合约里写入数据
// gasPrice ，gasLimit 两个参数，如果想用默认值可以不传，或者传 null
// 如果不传的话，两个参数都必须不传，要传就一起传， 如果设置为 null 的话，可以一个为 null ，一个有值
        SendResultModel sendResultModel = ethContract.sendRawTransaction(
                fromAddress, // 调用者的地址
                contractAddress, // 合约地址
                new BigInteger("1200000"), // gasPrice ，如果想用默认值 可以直接传 null ，或者不传这个参数
                new BigInteger("800000"), // gasLimit ，如果想用默认值 可以直接传 null ，或者不传这个参数
                ethAbiCodec.getInputData(
                        "transfer", // 要调用的方法名称
                        new Address(toAddress), // 方法的参数，如果有多个，可以继续传入下一个参数
                        new Uint256(new BigInteger("1000000000000000000")) // 方法的参数，如果有多个，可以继续传入下一个参数
                ) // 要调用的方法的 inputData
        );

        sendResultModel.getEthSendTransaction(); // 发送交易后的结果
        sendResultModel.getEthGetTransactionReceipt(); // 交易成功上链后的结果

    }

}
