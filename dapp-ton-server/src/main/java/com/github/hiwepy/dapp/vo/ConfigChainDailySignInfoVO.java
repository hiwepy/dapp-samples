
package com.github.hiwepy.dapp.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.hiwepy.dapp.entity.ConfigChainDailySign;
import io.server.domain.ton.ConfigActivityDailySign;
import lombok.Builder;
import lombok.Data;


@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder(builderClassName = "Builder", toBuilder = true)
public class ConfigChainDailySignInfoVO {

    @JsonProperty(value = "day")
    private Integer day;

    @JsonProperty(value = "reward")
    private String reward;


    public static ConfigChainDailySignInfoVO buildConfigDailySignInfoResp(ConfigChainDailySign dailySign) {
        return ConfigChainDailySignInfoVO.builder()
                .day(dailySign.getDay())
                .reward(dailySign.getRewardAmount().stripTrailingZeros().toPlainString())
                .build();
    }

}
