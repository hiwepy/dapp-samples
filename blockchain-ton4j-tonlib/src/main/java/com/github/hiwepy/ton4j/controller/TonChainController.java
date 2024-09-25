package com.github.hiwepy.ton4j.controller;

import com.github.hiwepy.ton4j.service.TonChainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ton")
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
    public Map generate(@RequestParam(value = "address") String address) {
        return Map.of("generation", tonChainService.signin(address));
    }

    /**
     * Ton_Proof 验证：根据前端传递的钱包地址，进行Ton_Proof验证
     * @param message
     * @return
     */
    @GetMapping("/v1/check")
    public List<Generation> check(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        return Map.of("generation", tonChainService.check(address));
    }

}
