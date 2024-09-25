package com.github.hiwepy.ton4j.controller;

import com.github.hiwepy.api.ApiRestResponse;
import com.github.hiwepy.ton4j.param.TonProofCheckReq;
import com.github.hiwepy.ton4j.service.TonChainService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/ton")
@Slf4j
public class TonChainController {

    private final TonChainService tonChainService;

    @Autowired
    public TonChainController(TonChainService tonChainService) {
        this.tonChainService = tonChainService;
    }

    /**
     * 链上签到：根据前端传递的钱包地址，进行链上签到
     * @param address 钱包地址
     * @return
     */
    @GetMapping("/v1/signin")
    public ApiRestResponse<Map> generate(@RequestParam(value = "address") String address) {
        /**
         * 1、检查用户是否已经签到
         */
        boolean signined = tonChainService.signin(address);
        if(signined){
            return ApiRestResponse.success(Map.of("signined", true));
        }
        return ApiRestResponse.success(Map.of("signined", false));
    }

    /**
     * 在服务器端检查 ton_proof
     * <a href="https://docs.ton.org/develop/dapps/ton-connect/sign#checking-ton_proof-on-server-side">在服务器端检查 ton_proof</a>
     * @return
     */
    @GetMapping("/check")
    public ApiRestResponse<Map> check(HttpServletRequest request,
                                            @RequestBody @Validated TonProofCheckReq req) {
        try {
            boolean checked = tonChainService.checkProof(req);
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
