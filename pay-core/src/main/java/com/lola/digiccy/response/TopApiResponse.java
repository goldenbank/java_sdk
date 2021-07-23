package com.lola.digiccy.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: shrimp
 * @Date: 2020/12/21 13:17
 */
@ApiModel("API响应结构")
public class TopApiResponse {
    @ApiModel("地址信息")
    @Data
    public static class Address implements Serializable {
        private static final long serialVersionUID = 2725214089541876602L;
        @ApiModelProperty("主币种编码")
        private Integer coinType;
        @ApiModelProperty("地址")
        private String address;
        @ApiModelProperty("回调地址")
        private String callUrl;
        @ApiModelProperty("别名")
        private String alias;
    }

    @ApiModel("提币单据")
    @Data
    public static class Order implements Serializable{
        private static final long serialVersionUID = -8151170687466023873L;
        @ApiModelProperty("单号")
        private String orderNo;
        @ApiModelProperty("转账地址")
        private String address;
        @ApiModelProperty("链上交易Id")
        private String txId;
        @ApiModelProperty("主币种编码")
        private String mainType;
        @ApiModelProperty("子币种编码/合约")
        private String subType;
        @ApiModelProperty("交易金额")
        private String amount;
        @ApiModelProperty("审核状态 0:待审核 1:待复审 2:待确认 3:人工确认 4:通过 5:驳回")
        private Integer status;
        @ApiModelProperty("审核时间")
        private Date trialTime;
        @ApiModelProperty("手续费")
        private String fee;
        @ApiModelProperty("商户单号")
        private String outerId;
        @ApiModelProperty("备注")
        private String memo;
        @ApiModelProperty("交易类型 1-充值 2-提币")
        private Integer tradeType;
        @ApiModelProperty("回调地址")
        private String callUrl;
    }

    @ApiModel("交易通知")
    @Data
    public static class TradeNotify implements Serializable{
        private static final long serialVersionUID = -8151170687466023873L;
        @ApiModelProperty("加密数据 md5(tradeData+apiKey)")
        private String sign;
        @ApiModelProperty("交易单据")
        private TradeData tradeData;
        @Data
        @ApiModel("交易单据")
        public static class TradeData{
            @ApiModelProperty("单号")
            private String tradeId;
            @ApiModelProperty("转账地址")
            private String address;
            @ApiModelProperty("链上交易Id")
            private String txId;
            @ApiModelProperty("主币种编码")
            private String mainType;
            @ApiModelProperty("子币种编码/合约")
            private String subType;
            @ApiModelProperty("交易金额")
            private String amount;
            @ApiModelProperty("交易类型 0-提币 1-充值")
            private Integer tradeType;
            @ApiModelProperty("审核状态")
            private Integer status;
            @ApiModelProperty("手续费")
            private String fee;
            @ApiModelProperty("币种精度")
            private Integer precisions;
            @ApiModelProperty("商户单号")
            private String outerId;
            @ApiModelProperty("备注")
            private String memo;
            @ApiModelProperty("区块高度")
            private Long blockHeight=-1L;
        }
    }

    @ApiModel("提币校验")
    @Data
    public static class TradeCheck implements Serializable{
        @ApiModelProperty("提币地址")
        private String address;
        @ApiModelProperty("交易金额")
        private String amount;
    }

    @ApiModel("币种区块链高度")
    @Data
    public static class BlockHeight implements Serializable{
        @ApiModelProperty("币种")
        private Integer coinType;

        @ApiModelProperty("最新高度")
        private Long lastHeight;
    }

    @Data
    @ApiModel("币种策略")
    public static class CoinStrategy implements Serializable{
        public CoinStrategy(){}
        public CoinStrategy(String mainType,String subType){
            this.mainType = mainType;
            this.subType = subType;
            this.id = this.mainType+"/"+this.subType;
        }
        @ApiModelProperty("唯一键")
        private String id;
        @ApiModelProperty("币种编码")
        private String mainType;
        @ApiModelProperty("子币种编码/合约")
        private String subType;
        @ApiModelProperty("合约、地址大小写处理 0-不做大小写处理 1-大写处理 2-小写处理")
        public Integer upOrLowerRule;
    }
}
