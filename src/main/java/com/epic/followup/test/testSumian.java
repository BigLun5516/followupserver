package com.epic.followup.test;

import static com.epic.followup.util.HTTPsUtils.getRequestBody;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.epic.followup.util.SumianHttpsUtils;
import com.epic.followup.util.UUIDUtil;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * @author : zx
 * @version V1.0
 */
public class testSumian {

    public static void main(String[] args) throws Exception{
        String sumianUrl = "https://sdapi-test.sumian.com";
        String sumianUserRegistUurl = "/c/users";
        String sumianUserTOkenUrl = "/c/login";
        String sumianAppKey = "75784a6d-afa9-4a30-9055-475f3c35a57c";
        String sumianAppSecret = "J1bCfS263QYmP9AmJoF1yY1Lzq2p0T0b";

        Map<String, String> params = new HashMap<String, String>();
        String time = Long.toString(new Date().getTime()/1000);
        String uuid = UUIDUtil.getUUID();
        params.put("App-Key", sumianAppKey);
        params.put("Nonce", uuid);
        params.put("Timestamp", time);
        params.put("Signature", DigestUtils.sha1Hex(sumianAppSecret+uuid+time));

        Map<String, String> bodyParams = new HashMap<String, String>();
        bodyParams.put("user_id", "3");
//        System.out.println(SumianHttpsUtils.postUserId(sumianUrl+sumianUserRegistUurl, params, bodyParams));
        System.out.println(SumianHttpsUtils.proxyStringSumianHttpRequest(sumianUrl+sumianUserTOkenUrl, "POST", params, getRequestBody(bodyParams)));
    }
}
