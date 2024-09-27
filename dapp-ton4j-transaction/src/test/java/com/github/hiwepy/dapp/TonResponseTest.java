package com.github.hiwepy.dapp;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

@Slf4j
public class TonResponseTest {

    /**
     * 测试地址解析
     *
     https://docs.ton.org/mandarin/learn/overviews/addresses

     https://toncenter.com/api/v2/#/transactions/get_transactions_getTransactions_get
     EQDKbjIcfM6ezt8KjKJJLshZJJSqX7XOA4ff-W72r5gqPrHF
     * @throws URISyntaxException
     */
    @Test
    public void testgetTransactionsResponse() throws URISyntaxException, IOException {

        URL resource = TonResponseTest.class.getResource("/getTransactions_response_1727449003057.json");
        File fiftFile = Paths.get(resource.toURI()).toFile();
        String responseJson = FileUtils.readFileToString(fiftFile);
        JSONObject jsonObject = JSONObject.parseObject(responseJson);
        log.info("jsonObject:{}",jsonObject);
        Boolean ok = jsonObject.getBoolean("ok");
        if (ok){
            JSONArray result = jsonObject.getJSONArray("result");
            for (int i = 0; i < result.size(); i++) {

                JSONObject transaction = result.getJSONObject(i);
                String type = transaction.getString("@type");
                JSONObject address = transaction.getJSONObject("address");
                log.info("address.type:{}", address.getString("@type"));
                log.info("account_address:{}", address.getString("account_address"));
                log.info("utime:{}", transaction.getLong("utime"));
                log.info("data:{}", transaction.getString("data"));

                JSONObject transaction_id = transaction.getJSONObject("transaction_id");
                log.info("transaction_id.type:{}", transaction_id.getString("@type"));
                log.info("transaction_id.it:{}", transaction_id.getString("it"));
                log.info("transaction_id.hash:{}", transaction_id.getString("hash"));


                log.info("fee:{}", transaction.getLong("fee"));
                log.info("storage_fee:{}", transaction.getLong("storage_fee"));
                log.info("other_fee:{}", transaction.getLong("other_fee"));

                JSONObject in_msg = transaction.getJSONObject("in_msg");
                log.info("in_msg.type:{}", in_msg.getString("@type"));
                log.info("in_msg.source:{}", in_msg.getString("source"));
                log.info("in_msg.destination:{}", in_msg.getString("destination"));
                log.info("in_msg.value:{}", in_msg.getString("value"));
                log.info("in_msg.fwd_fee:{}", in_msg.getString("fwd_fee"));
                log.info("in_msg.ihr_fee:{}", in_msg.getString("ihr_fee"));
                log.info("in_msg.created_lt:{}", in_msg.getLong("created_lt"));
                log.info("in_msg.body_hash:{}", in_msg.getString("body_hash"));
                JSONObject in_msg_msg_data = in_msg.getJSONObject("msg_data");
                log.info("in_msg.msg_data.type:{}", in_msg_msg_data.getString("@type"));
                log.info("in_msg.msg_data.text:{}", in_msg_msg_data.getString("text"));
                log.info("in_msg.message:{}", in_msg.getString("message"));

                JSONObject out_msgs = transaction.getJSONObject("in_msg");
                log.info("out_msgs.type:{}", out_msgs.getString("@type"));
                log.info("out_msgs.source:{}", out_msgs.getString("source"));
                log.info("out_msgs.destination:{}", out_msgs.getString("destination"));
                log.info("out_msgs.value:{}", out_msgs.getString("value"));
                log.info("out_msgs.fwd_fee:{}", out_msgs.getString("fwd_fee"));
                log.info("out_msgs.ihr_fee:{}", out_msgs.getString("ihr_fee"));
                log.info("out_msgs.created_lt:{}", out_msgs.getLong("created_lt"));
                log.info("out_msgs.body_hash:{}", out_msgs.getString("body_hash"));
                JSONObject out_msgs_msg_data = out_msgs.getJSONObject("msg_data");
                log.info("out_msgs.msg_data.type:{}", out_msgs_msg_data.getString("@type"));
                log.info("out_msgs.msg_data.text:{}", out_msgs_msg_data.getString("text"));
                log.info("out_msgs.message:{}", out_msgs_msg_data.getString("message"));


                log.info("===========================================================");
            }

        }
    }

}
