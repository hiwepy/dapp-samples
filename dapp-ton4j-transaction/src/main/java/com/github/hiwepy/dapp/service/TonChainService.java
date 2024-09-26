package com.github.hiwepy.dapp.service;

import com.github.hiwepy.dapp.param.TonProofCheckReq;

public interface TonChainService {

    /**
     * 在服务器端检查 ton_proof
     * @param appId 客户端ID
     * @param req 请求参数
     * @return 是否验证通过
     */
    boolean checkProof(String appId, TonProofCheckReq req);

}
