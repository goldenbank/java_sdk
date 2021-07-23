package com.lola.digiccy.constant;

/**
 * @Author: shrimp
 * @Date: 2020/12/21 14:28
 */
public class TopApiUrl {
    /**
     * 获取令牌
     */
    public static final String GENERATE_TOKEN = "/api/open/v1/generate-token";

    /**
     * 创建地址
     */
    public static final String CREATE_ADDRESS = "/api/open/v1/address/create";
    /**
     * 校验地址合法性
     */
    public static final String CHECK_ADDRESS = "/api/open/v1/address/check";

    /**
     * 获取币种策略
     */
    public static final String COIN_STRATEGY = "/api/open/v1/coin-strategy";

    /**
     * 单笔提币转账
     */
    public static final String SINGLE_TRANSFER = "/api/open/v1/transfer/single";

    /**
     * 查询单笔订单信息
     */
    public static final String SINGLE_ORDER_QUERY = "/api/open/v1/order/single";
    /**
     * 签名信息
     */
    public static final String SIGN_INFO = "/api/open/v1/merchant/sign";
}
