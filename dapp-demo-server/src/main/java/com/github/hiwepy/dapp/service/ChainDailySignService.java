
package com.github.hiwepy.dapp.service;

import com.github.hiwepy.dapp.param.ChainDailySignClaimReq;

import java.util.Map;

public interface ChainDailySignService {

    /**
     * 用户每日链上签到
     * @param req
     * @return
     */
    Map<String, String> dailySignClaim(String appId, ChainDailySignClaimReq req);

}
