package com.lola.digiccy.test;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lola.digiccy.client.ApiResult;
import com.lola.digiccy.client.IPayClient;
import com.lola.digiccy.client.PayClient;
import com.lola.digiccy.constant.CoinType;
import com.lola.digiccy.request.TopApiRequest;
import com.lola.digiccy.response.TopApiResponse;
import com.lola.digiccy.util.GeneratorUtil;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

/**
 * @Author: shrimp
 * @Date: 2020/12/21 19:42
 */
public class TestClient {
    private static IPayClient payClient;
    private static Gson gson;
    public static void main(String[] args) throws IOException {
        gson =new Gson();
        long merchantId=1382166960727490561L;
        String apiKey="3de9a232bdc7e42a3dcf2922d26a7bae";
        // String gatewayUrl="http://digiccy-gapi.yuanxkj.club/gateway";
        String gatewayUrl="http://127.0.0.1:6010/gateway";
        String walletNo="WT_1382166960727490561_qH88";
        payClient = new PayClient(merchantId,walletNo,apiKey,gatewayUrl,100000);
        //String token="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ7XCJhcGlLZXlcIjpcIkRwMTJkZHMxMjJkc2RcIixcImlwXCI6XCIxMjcuMC4wLjFcIixcIm1lcmNoYW50SWRcIjoxMzMxOTExMDA0Njg0NDc2NDE3LFwid2FsbGV0UHViS2V5XCI6XCJ4cHViNjlNazkzOUdob2QzQUdjdTVRN1BqaDQycFZVU0R3YWRIOE1VaVFXbUtqYXlFMXpjRHRlRTNNa0tQTEFBZ3RoUk5Ib1lZd1JWd1E1QkRwTjV2WGZEWHJOUDVGdmJoWndNeWJUTHlCOVA2RWpcIn0iLCJpYXQiOjE2MTgyMjQ1ODQsImV4cCI6MTYxODI2Nzc4NH0.b_1l862cJVTE3VJh6gr05SkTeMH2ldLZvZT0Edahdg6kKyZdENuvn9AzhBkPjIRnviuQwu-bRVvLKrJ5rneyQg";
        //payClient.setToken(token);
        // Token获取
//        for(int i=1;i<11;i++) {
//            ApiResult<String> tokenResult = payClient.generateToken();
//            if (tokenResult.getCode() == 200) {
//                System.out.println(tokenResult.getData());
////                payClient.setToken(tokenResult.getData());
//            }
//        }
        payClient.generateToken();
        getCoinStrategy();
        // 校验地址
        // checkAddress();


        //transfer(new BigDecimal("0.05"));

//        for(int i=1;i<31;i++){
//            BigDecimal amount = new BigDecimal("0.001"+i);
//            transfer(amount.multiply(BigDecimal.valueOf(i)));
//        }
        // System.out.println(gson.toJson(tokenResult));
        // System.out.println(tokenResult.getData());
//        for(int i=1;i<100;i++){
//            new Thread(()->{
//                try {
//                    createAddress();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }).start();
//        }
        // createAddress();
        // transfer();
    }

    public static void transfer(BigDecimal amount) throws IOException {
        // transfer();
        TopApiRequest.TransferRequest request = new TopApiRequest.TransferRequest();
        request.setAddress("0xe58f8770547BfA81916393cF76c2B5341EA68174");
        request.setAmount(amount);
        request.setCallUrl("http://digiccy-notify.yuanxkj.club/notify/");
        request.setMainType("60");
        request.setSubType("60");
        request.setOuterId(GeneratorUtil.getOrderId("K"));
        request.setMemo("测试备注");
        ApiResult<?> result = payClient.transfer(request);
        if(result.getCode()==200) {
            System.out.println(result.getCode() + result.getData().toString());
        }else{
            System.out.println(result.getCode()+"~"+result.getMessage());
        }
    }


    public static void createAddress() throws IOException {
        TopApiRequest.CreateAddressRequest request = new TopApiRequest.CreateAddressRequest();
        request.setCallUrl("http://mworld-api.yuanxkj.club/kin-dun-pay/notify");
        request.setCoinType(60);
        request.setAlias("ETH");
        ApiResult<?> result = payClient.createAddress(request);
        if(result.getCode()==200) {
            System.out.println(result.getCode() + result.getData().toString());
        }else{
            System.out.println(result.getCode()+"~"+result.getMessage());
        }
    }

    public static void checkAddress() throws IOException {
        TopApiRequest.CheckAddressRequest request =new TopApiRequest.CheckAddressRequest();
        request.setAddress("0xa5daba4ee61c7e26f67eb25396774ac92fd1b100");
        request.setCoinType(60);
        ApiResult<?> result = payClient.checkAddress(request);
        if(result.getCode()==200) {
            System.out.println(gson.toJson(result));
        }else{
            System.out.println(result.getCode()+"~"+result.getMessage());
        }
    }

    public static void getCoinStrategy() throws IOException {
        ApiResult<Map<String,TopApiResponse.CoinStrategy>> result = payClient.getCoinStrategy();
        System.out.println(gson.toJson(result));
    }
}
