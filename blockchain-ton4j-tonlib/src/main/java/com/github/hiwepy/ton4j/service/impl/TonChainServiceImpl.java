package com.github.hiwepy.ton4j.service.impl;

import com.github.hiwepy.ton4j.entity.User;
import com.github.hiwepy.ton4j.exception.BizExceptionCode;
import com.github.hiwepy.ton4j.param.TonChainSignReq;
import com.github.hiwepy.ton4j.param.TonProofCheckReq;
import com.github.hiwepy.ton4j.service.TonChainService;
import com.github.hiwepy.ton4j.service.UserService;
import com.iwebpp.crypto.TweetNaclFast;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ton.java.tonconnect.Domain;
import org.ton.java.tonconnect.TonConnect;
import org.ton.java.tonconnect.TonProof;
import org.ton.java.tonconnect.WalletAccount;
import org.ton.java.utils.Utils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;

@Service
@Slf4j
public class TonChainServiceImpl implements TonChainService {

    private final UserService userService;

    @Autowired
    public TonChainServiceImpl(UserService userService) {
        this.userService = userService;
    }

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
        try {

            // backend prepares the request
            // 1、从用户处检索 TonProofItemReply。
            TonProof tonProof = TonProof.builder()
                    .timestamp(Timestamp.valueOf(LocalDateTime.now()).getTime())
                    .domain(Domain.builder()
                            .value(req.getProof().getDomain().getValue())
                            .lengthBytes(req.getProof().getDomain().getLengthBytes())
                            .build())
                    .payload(req.getProof().getPayload())
                    .build();

            // wallet signs it
            byte[] secretKey = Utils.hexToSignedBytes("F182111193F30D79D517F2339A1BA7C25FDF6C52142F0F2C1D960A1F1D65E1E4");

            TweetNaclFast.Signature.KeyPair keyPair = TweetNaclFast.Signature.keyPair_fromSeed(secretKey);
            byte[] message = TonConnect.createMessageForSigning(tonProof, req.getAddress());
            byte[] signature = Utils.signData(keyPair.getPublicKey(), secretKey, message);
            log.info("signature: {}", Utils.bytesToHex(signature));

            // 通过增加签名来更新 TonProof
            tonProof.setSignature(Utils.bytesToBase64SafeUrl(signature));

            // 后端验证 ton proof 请求
            WalletAccount walletAccount = WalletAccount.builder()
                    .chain(-239)
                    .address(req.getAddress())
                    .publicKey("82a0b2543d06fec0aac952e9ec738be56ab1b6027fc0c1aa817ae14b4d1ed2fb")
                    .build();

            return TonConnect.checkProof(tonProof, walletAccount);
        } catch (Exception e) {
            log.error("verifying error", e);
            return Boolean.FALSE;
        }
    }

    @Override
    public boolean sign(String appId, TonChainSignReq req) {
        // 1、检查用户是否存在
        User user = userService.getById(req.getUserId());
        if (Objects.isNull(user)) {
            log.info("TonChainServiceImpl.sign userId:{}", req.getUserId());
            BizExceptionCode.USER_NOT_FOUND.throwException();
        }

        return false;
    }


}
