package com.lola.digiccy.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * API请求
 * @Author: shrimp
 * @Date: 2020/12/21 13:15
 */
@ApiModel("API请求")
public class TopApiRequest {
    @ApiModel("生成令牌")
    @Data
    public static class GenerateTokenRequest implements Serializable {
        private static final long serialVersionUID = 8789296094492224921L;
        @ApiModelProperty("商户ID")
        @NotNull(message = "MERCHANT_ID_IS_NULL")
        private Long merchantId;
        @ApiModelProperty("应用密匙")
        @NotNull(message = "APP_SECRET_IS_NULL")
        private String appSecret;
        @ApiModelProperty("钱包编码 (主钱包可不填)")
        @NotNull(message = "WALLET_NO_IS_NULL")
        private String walletNo;
        @ApiModelProperty("强制刷新 0-存在返回历史Token 1-强制获取新Token")
        private Integer isForce=0;
    }

    @ApiModel("请求创建地址")
    @Data
    public static class CreateAddressRequest implements Serializable {
        private static final long serialVersionUID = -8957050366487498685L;
        @NotNull(message = "COIN_TYPE_IS_NULL")
        @ApiModelProperty("币种")
        private Integer coinType;

        @ApiModelProperty("地址别名 (应用用户ID 建议商户唯一)")
        private String alias;

        @ApiModelProperty("回调地址")
        @NotNull(message = "CALL_URL_IS_NULL")
        private String callUrl;
    }

    @ApiModel("提币申请")
    @Data
    public static class TransferRequest implements Serializable{
        private static final long serialVersionUID = 459330910026929184L;
        @ApiModelProperty("提币地址")
        @NotNull(message = "ADDRESS_IS_NULL")
        private String address;
        @ApiModelProperty("数量")
        @NotNull(message = "AMOUNT_IS_NULL")
        private BigDecimal amount = BigDecimal.ZERO;
        @ApiModelProperty("主币种编码")
        @NotNull(message = "MAIN_TYPE_IS_NULL")
        private String mainType;
        @ApiModelProperty("子币种编码")
        @NotNull(message = "SUB_TYPE_IS_NULL")
        private String subType;
        @ApiModelProperty("回调地址 选填")
        private String callUrl;
        @ApiModelProperty("单据校验URL:填写则审核前需要反向校验审核是否合法")
        private String checkUrl;
        @ApiModelProperty("商户单号 商户内部系统唯一")
        @NotNull(message = "OUTER_ID_IS_NULL")
        private String outerId;
        @ApiModelProperty("备注")
        private String memo;
        @ApiModelProperty("区块链备注 EOS、XRP等币种上链使用")
        private String blockchainTag;
    }

    @ApiModel("提币代付申请")
    @Data
    public static class RepayTransferRequest implements Serializable{
        private static final long serialVersionUID = -1L;
        @ApiModelProperty("提币地址")
        @NotNull(message = "ADDRESS_IS_NULL")
        private String address;
        @ApiModelProperty("数量")
        @NotNull(message = "AMOUNT_IS_NULL")
        private String amount;
        @ApiModelProperty("主币种编码")
        @NotNull(message = "MAIN_TYPE_IS_NULL")
        private String mainType;
        @ApiModelProperty("子币种编码")
        @NotNull(message = "SUB_TYPE_IS_NULL")
        private String subType;
        @ApiModelProperty("回调地址 选填")
        private String callUrl;
        @ApiModelProperty("商户单号 商户内部系统唯一")
        @NotNull(message = "OUTER_ID_IS_NULL")
        private String outerId;
        @ApiModelProperty("备注")
        private String memo;
        @ApiModelProperty("签名")
        private String sign;
    }

    @ApiModel("校验地址")
    @Data
    public static class CheckAddressRequest implements Serializable{
        private static final long serialVersionUID = 5773876857827639324L;
        @ApiModelProperty("币种")
        @NotNull(message = "COIN_TYPE_IS_NULL")
        private Integer coinType;

        @ApiModelProperty("币种")
        @NotNull(message = "ADDRESS_IS_NULL")
        private String address;
    }

    @ApiModel("商户币种列表")
    @Data
    public static class SupportCoinRequest implements Serializable {
        private static final long serialVersionUID = -9170486749139738993L;
    }

    @ApiModel("获取指定对账单")
    @Data
    public static class OrderRequest implements Serializable{
        private static final long serialVersionUID = -875170213809913781L;
        @ApiModelProperty("主币种编码")
        @NotNull(message = "COIN_TYPE_IS_NULL")
        private Integer mainType;

        @ApiModelProperty("币种编码/合约地址")
        @NotNull(message = "SUB_TYPE_IS_NULL")
        private String subType;

        @ApiModelProperty("商户单号 商户内部系统唯一")
        @NotNull(message = "OUTER_ID_IS_NULL")
        private String outerId;
    }


    @ApiModel("获取指定币种区块高度")
    @Data
    public static class BlockHeightRequest implements  Serializable{
        @ApiModelProperty("币种")
        @NotNull(message = "COIN_TYPE_IS_NULL")
        private Integer coinType;
    }

}
