package com.lola.digiccy.constant;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author dengyejun
 */
public class TradeConstant {
    @ApiModel("业务类型")
    public static class TradeType{
        @ApiModelProperty("提币")
        public static int WITHDRAW = 0;

        @ApiModelProperty("充币")
        public static int RECHARGE=1;
    }

    @ApiModel("提币订单状态")
    public static class WithdrawStatus{
        @ApiModelProperty("等待中")
        public static final int WAITING=2;
        @ApiModelProperty("转账中")
        public static final int TRANSFERING=3;
        @ApiModelProperty("交易完成")
        public static final int COMPLETED=4;
        @ApiModelProperty("驳回")
        public static final int REJECT=5;
        @ApiModelProperty("交易失败")
        public static final int FAILED=6;
        @ApiModelProperty("发送失败")
        public static final int SEND_FAILED=7;
    }
}
