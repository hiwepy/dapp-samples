package com.github.hiwepy.ton4j.param;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;



@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class ChainDailySignClaimReq {

    @JsonProperty(value = "userId")
    @NotNull(message = "userId not null")
    private Long userId;

}
