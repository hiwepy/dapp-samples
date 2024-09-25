package com.github.hiwepy.dapp.param;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder(builderClassName = "Builder", toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class TonChainSignReq {

    @JsonProperty(value = "userId")
    @NotNull(message = "userId not null")
    private Long userId;

    @JsonProperty(value = "address")
    @NotBlank(message = "Ton Address Not Blank")
    private String address;

}
