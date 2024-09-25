package com.github.hiwepy.dapp.controller;

import com.github.hiwepy.api.ApiRestResponse;
import com.github.hiwepy.api.XHeaders;
import com.github.hiwepy.dapp.param.TonChainSignReq;
import com.github.hiwepy.dapp.vo.TonProofCheckReq;
import com.github.hiwepy.dapp.service.TonChainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/ton/v1")
@Slf4j
public class TonChainController {

    private final TonChainService tonChainService;


    @Autowired
    public TonChainController(TonChainService tonChainService) {
        this.tonChainService = tonChainService;
    }

    /**
     * 链上签到：根据前端传递的钱包地址，进行链上签到
     * @param appId 客户端ID
     * @param req 请求参数
     * @return
     */
    @GetMapping("/sign")
    public ApiRestResponse<Map> sign(@RequestHeader(XHeaders.X_APP_ID) String appId,
                                     @RequestBody @Validated TonChainSignReq req) {
        /**
         * 1、检查用户是否已经签到
         */
        boolean signed = tonChainService.sign(appId, req);
        if(signed){
            return ApiRestResponse.success(Map.of("signed", true));
        }
        return ApiRestResponse.success(Map.of("signed", false));
    }

    /**
     * 在服务器端检查 ton_proof
     * <a href="https://docs.ton.org/develop/dapps/ton-connect/sign#checking-ton_proof-on-server-side">在服务器端检查 ton_proof</a>
     * @param appId 客户端ID
     * @param req 请求参数
     * @return
     */
    @GetMapping("/check")
    public ApiRestResponse<Map> check(@RequestHeader(XHeaders.X_APP_ID) String appId,
                                      @RequestBody @Validated TonProofCheckReq req) {
        try {
            boolean checked = tonChainService.checkProof(appId, req);
            if(checked){
                return ApiRestResponse.success(Map.of("checked", true));
            }
            return ApiRestResponse.success(Map.of("checked", false));
        } catch (Exception e) {
            log.error("verifying error", e);
            return ApiRestResponse.fail("verifying error");
        }
    }



}
