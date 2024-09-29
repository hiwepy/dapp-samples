package com.github.hiwepy.dapp;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.hiwepy.api.util.DateUtils;
import com.github.hiwepy.api.util.JacksonUtils;
import com.github.hiwepy.api.util.OkHttpClientCreater;
import com.github.hiwepy.api.util.TimezoneEnum;
import com.github.hiwepy.dapp.util.TonHttpApiUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.stream.Stream;

@Slf4j
public class TonHttpApiTest {

    protected final static String APPLICATION_JSON_VALUE = "application/json";
    protected static OkHttpClient httpClient = OkHttpClientCreater.createOKHttpClient(HttpLoggingInterceptor.Level.BODY);

    public static final String TEST_ADDRESS_0 = "0QAs9VlT6S776tq3unJcP5Ogsj-ELLunLXuOb1EKcOQi4-QO";
    public static final String TEST_ADDRESS_1 = "kQAs9VlT6S776tq3unJcP5Ogsj-ELLunLXuOb1EKcOQi47nL";
    public static final String TEST_ADDRESS_3 = "0:2cf55953e92efbeadab7ba725c3f93a0b23f842cbba72d7b8e6f510a70e422e3";
    public static final String TEST_ADDRESS_4 = "kf_8uRo6OBbQ97jCx2EIuKm8Wmt6Vb15-KsQHFLbKSMiYIny";

    public static final String TEST_ADDRESS_5 = "-1:cdff07eb154c2e595930a6a9a4451608251cc1c894686c4c110894de43c96ad3";

    public static final String TEST_ADDRESS_6 = "UQAXoO7fPVzy2piMEgdcejkXCR3UrGBfNuZWcU5-e79Xk1cO"; // isWallet
    public static final String TEST_ADDRESS_7 = "EQDKbjIcfM6ezt8KjKJJLshZJJSqX7XOA4ff-W72r5gqPrHF"; // not wallet

    /**
     * 测试地址解析
     * @throws URISyntaxException
     */
    @Test
    public void testGetTransactionsResponseParser() throws IOException {

        String responseJson = TonHttpApiUtil.getTransactions(TEST_ADDRESS_7);

        JsonNode jsonNode = JacksonUtils.json2node(responseJson);
        log.info("jsonNode:{}", jsonNode);
        Boolean ok = jsonNode.get("ok").asBoolean();
        if (ok){
            JsonNode result = jsonNode.get("result");
            for (int i = 0; i < result.size(); i++) {

                JsonNode transaction = result.get(i);
                String type = transaction.get("@type").asText();
                log.info("utime:{}", transaction.hasNonNull("utime") ? transaction.get("utime").asLong(): StringUtils.EMPTY);
                log.info("data:{}", transaction.has("data") ? transaction.get("data").asText(): StringUtils.EMPTY);
                log.info("fee:{}",  transaction.hasNonNull("fee") ? transaction.get("fee").asLong(): StringUtils.EMPTY);
                log.info("storage_fee:{}",  transaction.hasNonNull("storage_fee") ? transaction.get("storage_fee").asLong(): StringUtils.EMPTY);
                log.info("other_fee:{}",  transaction.hasNonNull("other_fee") ? transaction.get("other_fee").asLong(): StringUtils.EMPTY);

                if (transaction.has("address")) {
                    JsonNode address = transaction.get("address");
                    log.info("address.type:{}", address.has("@type") ? address.get("@type").asText(): StringUtils.EMPTY);
                    log.info("account_address:{}", address.has("account_address") ? address.get("account_address").asText(): StringUtils.EMPTY);
                }

                if (transaction.has("transaction_id")) {
                    JsonNode transaction_id = transaction.get("transaction_id");
                    log.info("transaction_id.type:{}", transaction_id.has("@type") ? transaction_id.get("@type").asText(): StringUtils.EMPTY);
                    log.info("transaction_id.it:{}", transaction_id.has("lt") ? transaction_id.get("lt").asText(): StringUtils.EMPTY);
                    log.info("transaction_id.hash:{}", transaction_id.has("hash") ? transaction_id.get("hash").asText(): StringUtils.EMPTY);
                }

                if (transaction.has("in_msg")) {
                    JsonNode in_msg = transaction.get("in_msg");
                    log.info("in_msg.type:{}", in_msg.has("@type") ? in_msg.get("@type").asText(): StringUtils.EMPTY);
                    log.info("in_msg.source:{}", in_msg.has("source") ? in_msg.get("source").asText(): StringUtils.EMPTY);
                    log.info("in_msg.destination:{}", in_msg.has("destination") ? in_msg.get("destination").asText(): StringUtils.EMPTY);
                    log.info("in_msg.value:{}", in_msg.has("value") ? in_msg.get("value").asText(): StringUtils.EMPTY);
                    log.info("in_msg.fwd_fee:{}", in_msg.has("fwd_fee") ? in_msg.get("fwd_fee").asText(): StringUtils.EMPTY);
                    log.info("in_msg.ihr_fee:{}", in_msg.has("ihr_fee") ? in_msg.get("ihr_fee").asText(): StringUtils.EMPTY);
                    log.info("in_msg.created_lt:{}", in_msg.has("created_lt") ? in_msg.get("created_lt").asLong(): StringUtils.EMPTY);
                    log.info("in_msg.body_hash:{}", in_msg.has("body_hash") ? in_msg.get("body_hash").asText(): StringUtils.EMPTY);
                    if (in_msg.has("msg_data")) {
                        JsonNode in_msg_msg_data = in_msg.get("msg_data");
                        log.info("in_msg.msg_data.type:{}", in_msg_msg_data.has("@type") ? in_msg_msg_data.get("@type").asText(): StringUtils.EMPTY);
                        log.info("in_msg.msg_data.text:{}", in_msg_msg_data.has("text") ? in_msg_msg_data.get("text").asText(): StringUtils.EMPTY);
                    }
                    log.info("in_msg.message:{}", in_msg.has("message") ? in_msg.get("message").asText(): StringUtils.EMPTY);
                }

                if (transaction.has("out_msgs")) {
                    JsonNode out_msgs = transaction.get("out_msgs");
                    for (int i2 = 0; i2 < out_msgs.size(); i2++) {

                        JsonNode out_msg = out_msgs.get(i2);
                        log.info("out_msgs[{}].type:{}", i2, out_msg.has("@type") ? out_msg.get("@type").asText(): StringUtils.EMPTY);
                        log.info("out_msgs[{}].source:{}", i2, out_msg.has("source") ? out_msg.get("source").asText(): StringUtils.EMPTY);
                        log.info("out_msgs[{}].destination:{}", i2, out_msg.has("destination") ? out_msg.get("destination").asText(): StringUtils.EMPTY);
                        log.info("out_msgs[{}].value:{}", i2, out_msg.has("value") ? out_msg.get("value").asText(): StringUtils.EMPTY);
                        log.info("out_msgs[{}].fwd_fee:{}", i2, out_msg.has("fwd_fee") ? out_msg.get("fwd_fee").asText(): StringUtils.EMPTY);
                        log.info("out_msgs[{}].ihr_fee:{}", i2, out_msg.has("ihr_fee") ? out_msg.get("ihr_fee").asText(): StringUtils.EMPTY);
                        log.info("out_msgs[{}].created_lt:{}", i2, out_msg.has("created_lt") ? out_msg.get("created_lt").asLong(): StringUtils.EMPTY);
                        log.info("out_msgs[{}].body_hash:{}", i2, out_msg.has("body_hash") ? out_msg.get("body_hash").asText(): StringUtils.EMPTY);
                        if (out_msg.has("msg_data")) {
                            JsonNode out_msgs_msg_data = out_msg.get("msg_data");
                            log.info("out_msgs[{}].msg_data.type:{}", i2, out_msgs_msg_data.has("@type") ? out_msgs_msg_data.get("@type").asText(): StringUtils.EMPTY);
                            log.info("out_msgs[{}].msg_data.text:{}", i2, out_msgs_msg_data.has("text") ? out_msgs_msg_data.get("text").asText(): StringUtils.EMPTY);
                            log.info("out_msgs[{}].message:{}", i2, out_msgs_msg_data.has("message") ? out_msgs_msg_data.get("message").asText(): StringUtils.EMPTY);
                        }
                    }



                }

                log.info("===========================================================");
            }

        }


    }
    @Test
    public void testGetTransactionsResponseCheck() throws URISyntaxException, IOException {

        // 使用 UTC时区
        TimezoneEnum utcTimezoneEnum = TimezoneEnum.UTC;
        // 获取今日开始时间
        long thisDayStartAt = DateUtils.utcStartByZoneId(utcTimezoneEnum) - 86_400_000 * 260;

        String responseJson = TonHttpApiUtil.getTransactions(TEST_ADDRESS_7);

        boolean checked = checkResponse(responseJson, TEST_ADDRESS_7, thisDayStartAt);
        System.out.println(checked);

        //assertThat(checkResponse(responseJson, address, thisDayStartAt)).isTrue();

    }

    /**
     * 检查交易是否由指定地址发起
     * @param responseBody 响应体
     * @param address 钱包地址
     * @param thisDayStartAt 今日开始时间 utc
     * @return
     */
    public static boolean checkResponse(String responseBody, String address, long thisDayStartAt) {
        try {
            JsonNode jsonNode = JacksonUtils.json2node(responseBody);
            Boolean ok = jsonNode.get("ok").asBoolean();
            if (ok){
                JsonNode result = jsonNode.get("result");
                if(Objects.isNull(result) || result.size() == 0){
                    return Boolean.FALSE;
                }
                JsonNode[] transactions = JacksonUtils.node2Array(result);
                return Stream.of(transactions).filter(Objects::nonNull).anyMatch(transaction -> {

                    // 检查交易ID是否为空
                    if (!transaction.has("transaction_id")) {
                        return Boolean.FALSE;
                    }
                    JsonNode transaction_id = transaction.get("transaction_id");
                    log.info("transaction_id.type:{}", transaction_id.has("@type") ? transaction_id.get("@type").asText() : StringUtils.EMPTY);
                    log.info("transaction_id.it:{}", transaction_id.has("lt") ? transaction_id.get("lt").asText() : StringUtils.EMPTY);
                    log.info("transaction_id.hash:{}", transaction_id.has("hash") ? transaction_id.get("hash").asText() : StringUtils.EMPTY);
                    // 检查交易地址是否为空
                    if (!transaction.has("address")) {
                        return Boolean.FALSE;
                    }
                    JsonNode addressNode = transaction.get("address");
                    log.info("address.type:{}", addressNode.has("@type") ? addressNode.get("@type").asText() : StringUtils.EMPTY);
                    log.info("account_address:{}", addressNode.has("account_address") ? addressNode.get("account_address").asText() : StringUtils.EMPTY);

                    // 筛选出指定地址发起的交易
                    if(!addressNode.has("account_address") && addressNode.get("account_address").asText().equals(address)){
                        return Boolean.FALSE;
                    }

                    log.info("utime:{}", transaction.hasNonNull("utime") ? transaction.get("utime").asLong() : 0);
                    log.info("data:{}", transaction.has("data") ? transaction.get("data").asText() :StringUtils.EMPTY);
                    log.info("fee:{}", transaction.hasNonNull("fee") ? transaction.get("fee").asLong() : 0);
                    log.info("storage_fee:{}", transaction.hasNonNull("storage_fee") ? transaction.get("storage_fee").asLong() : 0);
                    log.info("other_fee:{}", transaction.hasNonNull("other_fee") ? transaction.get("other_fee").asLong() : 0);


                    if (transaction.has("in_msg")) {
                        JsonNode in_msg = transaction.get("in_msg");
                        log.info("in_msg.source:{}", in_msg.hasNonNull("source") ? in_msg.get("source").asText() : StringUtils.EMPTY);
                        log.info("in_msg.destination:{}", in_msg.hasNonNull("destination") ? in_msg.get("destination").asText() : StringUtils.EMPTY);
                        log.info("in_msg.value:{}", in_msg.hasNonNull("value") ? in_msg.get("value").asText() : StringUtils.EMPTY);
                        log.info("in_msg.fwd_fee:{}", in_msg.hasNonNull("fwd_fee") ? in_msg.get("fwd_fee").asText() : StringUtils.EMPTY);
                        log.info("in_msg.ihr_fee:{}", in_msg.hasNonNull("ihr_fee") ? in_msg.get("ihr_fee").asText() : StringUtils.EMPTY);
                        log.info("in_msg.created_lt:{}", in_msg.hasNonNull("created_lt") ? in_msg.get("created_lt").asLong() : StringUtils.EMPTY);
                        log.info("in_msg.body_hash:{}", in_msg.hasNonNull("body_hash") ? in_msg.get("body_hash").asText() : StringUtils.EMPTY);
                    }

                    if (transaction.has("out_msgs")) {

                        JsonNode[] out_msgs = JacksonUtils.node2Array(transaction.get("out_msgs"));

                        return Stream.of(out_msgs).filter(Objects::nonNull).anyMatch(out_msg -> {

                            String source = out_msg.hasNonNull("source") ? out_msg.get("source").asText() :StringUtils.EMPTY;
                            String destination = out_msg.hasNonNull("destination") ? out_msg.get("destination").asText() : StringUtils.EMPTY;
                            long created_lt = out_msg.hasNonNull("created_lt") ? out_msg.get("created_lt").asLong() : 0L;

                            log.info("out_msgs.type:{}", out_msg.hasNonNull("@type") ? out_msg.get("@type").asText() : StringUtils.EMPTY);
                            log.info("out_msgs.source:{}", source);
                            log.info("out_msgs.destination:{}", destination);
                            log.info("out_msgs.value:{}", out_msg.hasNonNull("value") ? out_msg.get("value").asText() : StringUtils.EMPTY);
                            log.info("out_msgs.fwd_fee:{}", out_msg.hasNonNull("fwd_fee") ? out_msg.get("fwd_fee").asText() : StringUtils.EMPTY);
                            log.info("out_msgs.ihr_fee:{}", out_msg.hasNonNull("ihr_fee") ? out_msg.get("ihr_fee").asText() : StringUtils.EMPTY);
                            log.info("out_msgs.created_lt:{}", created_lt);
                            log.info("out_msgs.body_hash:{}", out_msg.hasNonNull("body_hash") ? out_msg.get("body_hash").asText() : StringUtils.EMPTY);
                            /*
                             *
                             * 1、from 地址和 to 地址均不能为空
                             * 2、检查交易是否由指定地址发起
                             * 3、交易时间大于等于今日开始时间  && created_lt >= thisDayStartAt
                             */
                            return StringUtils.isNotBlank(source) && StringUtils.isNotBlank(destination)
                                    && source.equals(address)
                                    && created_lt >= thisDayStartAt;
                        });

                    }
                    return Boolean.FALSE;
                });
            } else {
                log.info("get transactions failed from ton，code: {}, error : {}", jsonNode.get("error").asText());
                return Boolean.FALSE;
            }
        } catch (JacksonException e) {
            log.error("Jackson Parser Error : {}", e.getMessage());
            return Boolean.FALSE;
        }
    }

}
