package com.github.hiwepy.dapp.service.impl;

import com.github.hiwepy.dapp.param.TonProofCheckReq;
import com.github.hiwepy.dapp.service.TonChainService;
import com.github.hiwepy.dapp.util.TonConnectUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TonChainServiceImpl implements TonChainService {

    /**
     * 在服务器端检查 ton_proof
     * <a href="https://docs.ton.org/develop/dapps/ton-connect/sign#checking-ton_proof-on-server-side">在服务器端检查 ton_proof</a>
     * @return
     */
    @Override
    public boolean checkProof(String appId, TonProofCheckReq req) {
        /**
         * 1、从用户处检索 TonProofItemReply。
         * 2、验证接收到的域是否对应于应用程序的域。
         * 3、检查 TonProofItemReply.payload 是否被原始服务器允许且仍然有效。
         * 4、检查 timestamp 在当前是否真实。
         * 5、根据消息方案组装消息。
         * 6、通过API (a) 或 (b) 在后端实现的逻辑获取 public_key
         * 6a:
         *    通过 TON API 方法 POST /v2/tonconnect/stateinit 从 walletStateInit 中检索 {public_key, address}。
         *    验证从 walletStateInit 提取的 address 或对应于用户声明的钱包 address。
         * 6b:
         *   通过钱包合约的 get 方法 获得钱包的 public_key。
         *   如果合约未激活，或者缺少在旧钱包版本（v1-v3）中发现的 get_method，则以这种方式获取密钥将是不可能的。相反，您将需要解析前端提供的 walletStateInit。确保 TonAddressItemReply.walletStateInit.hash() 等于 TonAddressItemReply.address.hash()，表示一个BoC哈希。
         * 7、验证前端的 signature 实际上签署了组装的消息，并且对应于地址的 public_key。
         */
        ;
        try {
            return TonConnectUtil.checkProof(appId, req);
        } catch (Exception e) {
            log.error("verifying error", e);
            return Boolean.FALSE;
        }
    }

}
