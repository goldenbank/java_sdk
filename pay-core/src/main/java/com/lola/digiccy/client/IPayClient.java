package com.lola.digiccy.client;

import com.lola.digiccy.request.TopApiRequest;
import com.lola.digiccy.response.TopApiResponse;

import java.io.IOException;
import java.util.Map;

/**
 * @Author: shrimp
 * @Date: 2020/12/21 18:09
 */
public interface IPayClient {
    void setToken(String token);

    /**
     * 获取令牌
     *
     * @return
     */
    ApiResult<String> generateToken() throws IOException;

    /**
     * 获取地址
     *
     * @param request
     * @return
     */
    ApiResult<TopApiResponse.Address> createAddress(TopApiRequest.CreateAddressRequest request) throws IOException;

    /**
     * 提币转账申请
     *
     * @param request
     * @return
     */
    ApiResult<?> transfer(TopApiRequest.TransferRequest request) throws IOException;

    /**
     * 获取指定转账单据信息
     *
     * @param request
     * @return
     */
    ApiResult<TopApiResponse.Order> querySingleOrder(TopApiRequest.OrderRequest request) throws IOException;


    /**
     * 校验地址信息
     *
     * @param request
     * @return
     */
    ApiResult<?> checkAddress(TopApiRequest.CheckAddressRequest request) throws IOException;

    /**
     * 校验签名
     *
     * @param body
     * @param apiKey
     * @param sign
     * @return
     */
    Boolean checkSign(String body, String apiKey, String sign) throws Exception;


    /**
     * 获取币种策略
     * @return
     */
    ApiResult<Map<String,TopApiResponse.CoinStrategy>> getCoinStrategy() throws IOException;
}

