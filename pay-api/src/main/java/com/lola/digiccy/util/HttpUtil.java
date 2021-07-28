package com.lola.digiccy.util;

import com.alibaba.fastjson.JSON;
import com.lola.digiccy.response.TopApiResponse;
import okhttp3.*;
import okhttp3.OkHttpClient;
import java.util.concurrent.TimeUnit;

/**
 * @author MECHREVO
 */
public class HttpUtil {
    private  static  final Integer SUCCESS=200;
    public static final MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
    public static String callBack(String url,  TopApiResponse.TradeNotify tradeNotify) throws Exception {
        OkHttpClient client = new OkHttpClient.Builder().readTimeout(60, TimeUnit.SECONDS).build();
        String result = null;
        RequestBody requestBody = RequestBody.create(JSON.toJSONString(tradeNotify), mediaType);
        Request request = new Request.Builder().post(requestBody).url(url).build();
        Call call = client.newCall(request);
        Response response = call.execute();
        if (response.code() == SUCCESS) {
            result = response.body().string();
        }
        return result;
    }
}
