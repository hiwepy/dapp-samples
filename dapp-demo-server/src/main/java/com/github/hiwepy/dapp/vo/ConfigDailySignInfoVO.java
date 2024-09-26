
package com.github.hiwepy.dapp.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.hiwepy.dapp.entity.ConfigChainDailySign;
import lombok.Builder;
import lombok.Data;


@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder(builderClassName = "Builder", toBuilder = true)
public class ConfigDailySignInfoVO {

    @JsonProperty(value = "day")
    private Integer day;

    @JsonProperty(value = "reward")
    private String reward;

    public static ConfigDailySignInfoVO buildConfigDailySignInfoResp(ConfigChainDailySign dailySign) {
        return ConfigDailySignInfoVO.builder()
                .day(dailySign.getDay())
                .reward(dailySign.getReward().stripTrailingZeros().toPlainString())
                .build();
    }

}
