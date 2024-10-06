package com.github.hiwepy.dapp.param;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class TonTransactionCheckReq {

    /**
     * 钱包地址
     */
    @JsonProperty(value = "address")
    @NotBlank(message = "address not blank")
    private String address;

    /**
     * 交易hash地址
     */
    @JsonProperty(value = "hashAddress")
    @NotBlank(message = "hashAddress not blank")
    private String hashAddress;

}
