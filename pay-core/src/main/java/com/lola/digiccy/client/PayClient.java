package com.lola.digiccy.client;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lola.digiccy.constant.TopApiUrl;
import com.lola.digiccy.request.TopApiRequest;
import com.lola.digiccy.response.TopApiResponse;
import com.lola.digiccy.util.GeneratorUtil;
import lombok.Data;
import okhttp3.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 *
 * @Author: shrimp
 * @Date: 2020/12/21 18:07
 */
@Data
public class PayClient implements IPayClient {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private Gson gson;
    public final MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
    private final int SUCCESS=200;
    private OkHttpClient client;
    /**
     * 过期时间
     */
    private Date expireDate;
    /**
     * 过期间隔 秒
     */
    private int tokenExpire;
    /**
     * 网关地址
     */
    private String gatewayUrl;
    /**
     * 商户Id
     */
    private long merchantId;
    /**
     * 钱包编码
     */
    private String walletNo;
    /**
     * 密钥
     */
    private String apiKey;
    /**
     * 超时时间 分钟
     */
    private int timeout = 5;
    /**
     * 令牌
     */
    private String token;

    /**
     * 实例化
     * @param merchantId
     * @param apiKey
     * @param gatewayUrl
     * @param tokenExpire
     */
    public PayClient(long merchantId,String walletNo,String apiKey,String gatewayUrl,int tokenExpire) {
        client = new OkHttpClient.Builder().readTimeout(timeout, TimeUnit.MINUTES).build();
        this.merchantId = merchantId;
        gson = new Gson();
        this.apiKey = apiKey;
        this.gatewayUrl = gatewayUrl;
        this.tokenExpire=tokenExpire;
        this.walletNo = walletNo;
    }

    public PayClient() {
        client = new OkHttpClient.Builder().readTimeout(timeout, TimeUnit.MINUTES).build();
        gson = new Gson();
    }

    /**
     * 生成令牌
     * @return
     * @throws IOException
     */
    @Override
    public ApiResult<String> generateToken() throws IOException {
        String path = TopApiUrl.GENERATE_TOKEN;
        Date nowDate = new Date();
        TopApiRequest.GenerateTokenRequest request =new TopApiRequest.GenerateTokenRequest();
        request.setMerchantId(merchantId);
        request.setAppSecret(apiKey);
        request.setWalletNo(walletNo);
        String body = gson.toJson(request);
        RequestBody requestBody = RequestBody.create(body, mediaType);

        Type typeOfT = new TypeToken<ApiResult<String>>() {
        }.getType();
        ApiResult<String> result = dealHttp(requestBody, path, body, typeOfT,false);
        if(result.getCode()==SUCCESS) {
            //过期时间
            this.expireDate = new Date(nowDate.getTime() + tokenExpire * 1000);
            this.token = result.getData();
        }else{
            this.token="N";
        }
        return result;
    }

    /**
     * 创建地址
     * @param request
     * @return
     */
    @Override
    public ApiResult<TopApiResponse.Address> createAddress(TopApiRequest.CreateAddressRequest request) throws IOException {
        String path = TopApiUrl.CREATE_ADDRESS;
        String body = gson.toJson(request);
        RequestBody requestBody = RequestBody.create(body, mediaType);
        Type typeOfT = new TypeToken<ApiResult<TopApiResponse.Address>>() {
        }.getType();
        return dealHttp(requestBody, path, body, typeOfT);
    }

    /**
     * 发起单笔提币转账申请
     * @param request
     * @return
     */
    @Override
    public ApiResult<?> transfer(TopApiRequest.TransferRequest request) throws IOException {
        String path = TopApiUrl.SINGLE_TRANSFER;
        String body = gson.toJson(request);
        RequestBody requestBody = RequestBody.create(body, mediaType);
        return dealHttp(requestBody, path, body, null);
    }

    /**
     * 获取单笔转账单据明细
     * @param request
     * @return
     * @throws IOException
     */
    @Override
    public ApiResult<TopApiResponse.Order> querySingleOrder(TopApiRequest.OrderRequest request) throws IOException {
        String path = TopApiUrl.SINGLE_ORDER_QUERY;
        String body = gson.toJson(request);
        RequestBody requestBody = RequestBody.create(body, mediaType);

        Type typeOfT = new TypeToken<ApiResult<TopApiResponse.Order>>() {
        }.getType();
        return dealHttp(requestBody, path, body, typeOfT);
    }

    @Override
    public ApiResult<?> checkAddress(TopApiRequest.CheckAddressRequest request) throws IOException {
        String path = TopApiUrl.CHECK_ADDRESS;
        String body = gson.toJson(request);
        RequestBody requestBody = RequestBody.create(body, mediaType);

        Type typeOfT = new TypeToken<ApiResult<TopApiResponse.Address>>() {
        }.getType();
        return dealHttp(requestBody, path, body, typeOfT);
    }

    @Override
    public ApiResult<Map<String,TopApiResponse.CoinStrategy>> getCoinStrategy() throws IOException {
        String path = TopApiUrl.COIN_STRATEGY;
        String body = "";
        RequestBody requestBody = RequestBody.create(body, mediaType);

        Type typeOfT = new TypeToken<ApiResult<Map<String,TopApiResponse.CoinStrategy>>>() {
        }.getType();
        return dealHttp(requestBody, path, body, typeOfT);
    }

    private <T> ApiResult<T> dealHttp(RequestBody requestBody, String path, String body, Type typeOfT) throws IOException {
        return dealHttp(requestBody,path,body,typeOfT,true);
    }

    private <T> ApiResult<T> dealHttp(RequestBody requestBody, String path, String body, Type typeOfT,Boolean hasToken) throws IOException {
        if(hasToken){
            // 校验Token
            if(StringUtils.isEmpty(this.token) || expireDate==null || System.currentTimeMillis()>expireDate.getTime()){
                generateToken();
            }
            if("N".equals(this.token)){
                return ApiResult.error("OPEN_TOKEN_IS_ERROR");
            }
        }
        String reqUrl = gatewayUrl+path;
        logger.info("URL【{}】",reqUrl);
        Request request = new Request.Builder().post(requestBody).url(reqUrl).headers(buildHeaders(body)).build();
        Call call = client.newCall(request);
        Response response = call.execute();
        String json = response.body().string();
        if(response.code()== SUCCESS) {
            ApiResult<T> result;
            if (typeOfT != null) {
                result = gson.fromJson(json, typeOfT);
            }else {
                result = gson.fromJson(json, ApiResult.class);
            }
            switch (result.getCode()) {
                case 50007: // APP_TOKEN_IS_NULL
                case 50008: // APP_TOKEN_ERROR
                case 50009: // APP_TOKEN_EXPIRE
                case 50002: // SIGN_ERROR
                    // 令牌失效 重新获取令牌
                    generateToken();
                    break;
                default:
                    return result;
            }
            return result;
        }
        return ApiResult.error(response.message());
    }

    private Headers buildHeaders(String body){
        Map<String,String> headerMap =new HashMap<>();
        String nonce = GeneratorUtil.getNonceString(6);
        String timestamp = String.valueOf(System.currentTimeMillis());
        headerMap.put("merchantId",String.valueOf(merchantId));
        headerMap.put("walletNo",walletNo);
        headerMap.put("nonce",nonce);
        headerMap.put("timestamp",timestamp);
        if(body==null){
            // 防止null转字符串
            body="";
        }
        String signStr =token + merchantId + nonce + timestamp + body;
        String sign = DigestUtils.md5Hex(signStr).toLowerCase();
         //System.out.println("签名原文【"+signStr+"】签名【"+sign+"】");
        logger.info("签名原文【{}】签名【{}】",signStr,sign);
        headerMap.put("sign",sign);
        return okhttp3.Headers.of(headerMap);
    }

    @Override
    public Boolean checkSign(String body,String apiKey,String sign) throws Exception {
        String checkSign = DigestUtils.md5Hex(apiKey + body).toLowerCase();
        System.out.println(checkSign+"===="+sign);
        return checkSign.equals(sign);
    }




    @Override
    public void setToken(String token) {
        this.token = token;
    }
}
