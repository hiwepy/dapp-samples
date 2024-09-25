package com.github.hiwepy.ton4j.controller;

import com.github.hiwepy.api.ApiRestResponse;
import com.github.hiwepy.api.XHeaders;
import com.github.hiwepy.ton4j.entity.ConfigChainDailySign;
import com.github.hiwepy.ton4j.param.ChainDailySignClaimReq;
import com.github.hiwepy.ton4j.param.TonChainSignReq;
import com.github.hiwepy.ton4j.param.TonProofCheckReq;
import com.github.hiwepy.ton4j.service.ChainDailySignLogService;
import com.github.hiwepy.ton4j.service.ChainDailySignUserService;
import com.github.hiwepy.ton4j.service.ConfigChainDailySignService;
import com.github.hiwepy.ton4j.service.TonChainService;
import com.github.hiwepy.ton4j.vo.ConfigDailySignInfoVO;
import com.google.common.collect.Lists;
import jakarta.servlet.http.HttpServletRequest;
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
    private final ChainDailySignUserService chainDailySignUserService;
    private final ChainDailySignLogService chainDailySignLogService;


    @Autowired
    public TonChainController(TonChainService tonChainService,
                              ConfigChainDailySignService configChainDailySignService,
                              ChainDailySignUserService chainDailySignUserService,
                              ChainDailySignLogService chainDailySignLogService) {
        this.tonChainService = tonChainService;
        this.configChainDailySignService = configChainDailySignService;
        this.chainDailySignUserService = chainDailySignUserService;
        this.chainDailySignLogService = chainDailySignLogService;
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

    @GetMapping("/daily-sign-config")
    public ApiRestResponse<List<ConfigDailySignInfoVO>> getDailySignInfos(HttpServletRequest request) {
        List<ConfigChainDailySign> result = configChainDailySignService.getChainDailySignInfos();
        List<ConfigDailySignInfoVO> respList = Lists.newArrayList();
        for (ConfigChainDailySign dailySign : result) {
            respList.add(ConfigDailySignInfoVO.buildConfigDailySignInfoResp(dailySign));
        }
        return ApiRestResponse.success(respList);
    }

    @PostMapping("/daily-sign-claim")
    public ApiRestResponse<Map<String, String>> dailySignClaim(HttpServletRequest request,
                                                           @RequestBody @Validated ChainDailySignClaimReq req) {


        Result<Map<String, String>> result = activityService.dailySignClaim(req);
        if (result.isFailure()) {
            return ApiResponse.wrap(result);
        }
        return ApiResponse.success(result.getValue());
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
