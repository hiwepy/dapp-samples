package com.github.hiwepy.ton4j.service;

import com.github.hiwepy.ton4j.param.TonProofCheckReq;

public interface TonChainService {

    /**
     * 链上签到：根据前端传递的钱包地址，进行链上签到
     * @param address 钱包地址
     * @return 是否签到成功
     */
    boolean signin(String address);

    /**
     * 在服务器端检查 ton_proof
     * @param req 请求参数
     * @return 是否验证通过
     */
    boolean checkProof(TonProofCheckReq req);

}
