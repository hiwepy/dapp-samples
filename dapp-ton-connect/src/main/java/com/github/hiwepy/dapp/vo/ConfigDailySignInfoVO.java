
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
public class ConfigDailySignInfoVO {

    @JsonProperty(value = "days")
    private Integer days;

    @JsonProperty(value = "reward")
    private String reward;


    public static ConfigDailySignInfoVO buildConfigDailySignInfoResp(ConfigChainDailySign dailySign) {
        return ConfigDailySignInfoVO.builder()
                .days(dailySign.getDays())
                .reward(dailySign.getRewardAmount().stripTrailingZeros().toPlainString())
                .build();
    }

}
