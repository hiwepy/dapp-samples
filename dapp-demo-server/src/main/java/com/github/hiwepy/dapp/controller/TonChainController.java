package com.github.hiwepy.dapp.controller;

import com.github.hiwepy.api.ApiRestResponse;
import com.github.hiwepy.api.XHeaders;
import com.github.hiwepy.dapp.entity.ConfigChainDailySign;
import com.github.hiwepy.dapp.param.ChainDailySignClaimReq;
import com.github.hiwepy.dapp.param.TonProofCheckReq;
import com.github.hiwepy.dapp.service.ChainDailySignService;
import com.github.hiwepy.dapp.service.ConfigChainDailySignService;
import com.github.hiwepy.dapp.service.TonChainService;
import com.github.hiwepy.dapp.util.TonConnectUtil;
import com.github.hiwepy.dapp.vo.ConfigChainDailySignInfoVO;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ton/v1")
@Slf4j
public class TonChainController {

    private final TonChainService tonChainService;
    private final ConfigChainDailySignService configChainDailySignService;
    private final ChainDailySignService chainDailySignService;

    @Autowired
    public TonChainController(TonChainService tonChainService,
                              ConfigChainDailySignService configChainDailySignService,
                              ChainDailySignService chainDailySignService) {
        this.tonChainService = tonChainService;
        this.configChainDailySignService = configChainDailySignService;
        this.chainDailySignService = chainDailySignService;
    }

    /**
     * 获取离线
     * @return
     */
    @GetMapping("/daily-sign-config")
    public ApiRestResponse<List<ConfigChainDailySignInfoVO>> getDailySignInfos() {
        List<ConfigChainDailySign> result = configChainDailySignService.getChainDailySignInfos();
        List<ConfigChainDailySignInfoVO> respList = Lists.newArrayList();
        for (ConfigChainDailySign dailySign : result) {
            respList.add(ConfigChainDailySignInfoVO.buildConfigDailySignInfoResp(dailySign));
        }
        return ApiRestResponse.success(respList);
    }

    @PostMapping("/daily-sign-claim")
    public ApiRestResponse<Map<String, String>> dailySignClaim(@RequestHeader(XHeaders.X_APP_ID) String appId,
                                                               @RequestBody @Validated ChainDailySignClaimReq req) {
        Map<String, String> result = chainDailySignService.dailySignClaim(appId, req);
        return ApiRestResponse.success(result);
    }

    /**
     * 在服务器端检查 ton_proof
     * <a href="https://docs.ton.org/develop/dapps/ton-connect/sign#checking-ton_proof-on-server-side">在服务器端检查 ton_proof</a>
     * @param appId 客户端ID
     * @param req 请求参数
     * @return
     */
    @GetMapping("/check_ton_proof")
    public ApiRestResponse<Map> checkTonProof(@RequestHeader(XHeaders.X_APP_ID) String appId,
                                              @RequestBody @Validated TonProofCheckReq req) {
        try {
            boolean checked = TonConnectUtil.checkProof(appId, req);
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
