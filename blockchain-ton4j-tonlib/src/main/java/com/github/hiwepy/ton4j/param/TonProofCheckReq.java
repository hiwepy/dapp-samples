package com.github.hiwepy.ton4j.param;

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
public class TonProofCheckReq {

    @JsonProperty(value = "name")
    @NotBlank(message = "name Not Blank")
    private String name;

    @JsonProperty(value = "address")
    @NotBlank(message = "Ton Address Not Blank")
    private String address;

    @JsonProperty(value = "proof")
    @NotBlank(message = "proof Not Null")
    private TonProof proof;

    @Data
    public static class TonProof {

        @JsonProperty(value = "domain")
        @NotBlank(message = "domain Not Null")
        private TonProofDomain domain;
        /**
         * base64编码的签名
         */
        @JsonProperty(value = "signature")
        @NotBlank(message = "signature Not Blank")
        private String signature;
        /**
         * 来自请求的有效载荷
         */
        @JsonProperty(value = "payload")
        @NotBlank(message = "payload Not Blank")
        private String payload;

        @JsonProperty(value = "timestamp")
        @NotNull(message = "timestamp Not Null")
        private Long timestamp;

    }

    @Data
    public static class TonProofDomain {
        /**
         *  AppDomain 长度
         */
        @JsonProperty(value = "lengthBytes")
        @NotBlank(message = "lengthBytes Not Null")
        private Integer lengthBytes;
        /**
         * app 域名（作为url部分，无编码）
         */
        @JsonProperty(value = "value")
        @NotBlank(message = "value Not Blank")
        private String value;

    }
}
