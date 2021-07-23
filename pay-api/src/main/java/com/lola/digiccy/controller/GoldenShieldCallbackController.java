package com.lola.digiccy.controller;

import com.alibaba.fastjson.JSON;
import com.lola.digiccy.client.PayClient;
import com.lola.digiccy.constant.TradeConstant;
import com.lola.digiccy.response.TopApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;


/**
 * <p>
 * 金盾钱包回调Api
 * </p>
 *
 * @author Micheal
 * @since 2021/3/16
 */
@RestController
 @RequestMapping(value = "/", produces = "application/json;charset=utf-8")
public class GoldenShieldCallbackController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
 /*   private final PayClient payClient;

    public GoldenShieldCallbackController(PayClient payClient) {
        this.payClient = payClient;
    }*/

    @RequestMapping("/kin-dun-pay/notify")
    public String callBack(@RequestBody TopApiResponse.TradeNotify tradeNotify) throws Exception {
        boolean deal = true;
        //签名校验
       /* if (!payClient.checkSign(JSON.toJSONString(tradeNotify.getTradeData()), payClient.getApiKey(), tradeNotify.getSign())) {
            logger.error("验签失败,签名信息不一致,原签名信息【{}】", tradeNotify.getSign());
            return "error";
        }*/

        //构建payTrade公共对象
        //提币请求......
        if (tradeNotify.getTradeData().getTradeType() == TradeConstant.TradeType.WITHDRAW) {
            logger.info("提币请求！");
            this.logger.info("address:{},amount:{},mainCoinType:{},businessId:{}"
                    , tradeNotify.getTradeData().getAddress(), tradeNotify.getTradeData().getAmount(), tradeNotify.getTradeData().getMainType(), tradeNotify.getTradeData().getOuterId());
            // 提币处理
            deal = dealWithdraw(tradeNotify.getTradeData());
        }
        //充币请求......
        if (tradeNotify.getTradeData().getTradeType() == TradeConstant.TradeType.RECHARGE) {
            logger.info("=====收到充币通知======");
            logger.info("address:{},amount:{},mainCoinType:{},fee:{}", tradeNotify.getTradeData().getAddress(), tradeNotify.getTradeData().getAmount(), tradeNotify.getTradeData().getMainType(), tradeNotify.getTradeData().getFee());
            //失败记录
            if (tradeNotify.getTradeData().getStatus() == 6) {
                logger.error("交易记录[{}],业务编号[{}],地址[{}],金额[{}],状态[{}]是一笔失败记录,不处理", tradeNotify.getTradeData().getTxId(), tradeNotify.getTradeData().getOuterId(), tradeNotify.getTradeData().getAddress(), tradeNotify.getTradeData().getAmount(), tradeNotify.getTradeData().getStatus());
                return "error";
            } else {
                // 充币处理
                deal = dealRecharge(tradeNotify.getTradeData());
            }
        }
        if (deal) {
            return "success";
        }
        return "error";
    }

    /**
     * 提币处理
     * @param trade
     * @return
     */
    private boolean dealWithdraw(TopApiResponse.TradeNotify.TradeData trade) {
        // 币种精度 （手续费精度需考虑主币精度）
        int feeScale = trade.getPrecisions();
        BigDecimal amount = new BigDecimal(trade.getAmount()).divide(BigDecimal.TEN.pow(feeScale), feeScale, RoundingMode.DOWN);
        // 手续费精度需考虑主币精度
        BigDecimal fee = new BigDecimal(trade.getFee()).divide(BigDecimal.TEN.pow(feeScale), feeScale, RoundingMode.DOWN);

        switch (trade.getStatus()) {
            case TradeConstant.WithdrawStatus.TRANSFERING: {
                logger.info("业务编号[{}],地址[{}],金额[{}],状态[{}]审核通过", trade.getOuterId(), trade.getAddress(), trade.getAmount(), trade.getStatus());
                // TODO 审核通过 商户业务处理
                break;
            }
            case TradeConstant.WithdrawStatus.REJECT: {
                logger.warn("业务编号[{}],地址[{}],金额[{}],状态[{}]审核驳回", trade.getOuterId(), trade.getAddress(), trade.getAmount(), trade.getStatus());
                // TODO 审核驳回 商户业务处理
                break;
            }
            case TradeConstant.WithdrawStatus.COMPLETED: {
                logger.error("交易记录[{}],业务编号[{}],地址[{}],金额[{}],状态[{}]交易完成", trade.getTxId(), trade.getOuterId(), trade.getAddress(), trade.getAmount(), trade.getStatus());
                // TODO 交易完成 商户业务处理
                break;
            }
            case TradeConstant.WithdrawStatus.FAILED:
            case TradeConstant.WithdrawStatus.SEND_FAILED: {
                logger.error("交易记录[{}],业务编号[{}],地址[{}],金额[{}],状态[{}]交易失败", trade.getTxId(), trade.getOuterId(), trade.getAddress(), trade.getAmount(), trade.getStatus());
                // TODO 交易失败 商户业务处理
                break;
            }
            default:
                logger.warn("未处理状态【{}】", trade.getStatus());
                return false;
        }
        return true;
    }

    /**
     * 充币处理
     * @param trade
     * @return
     */
    private boolean dealRecharge(TopApiResponse.TradeNotify.TradeData trade){
        // 币种精度 （手续费精度需考虑主币精度）
        int feeScale = trade.getPrecisions();
        BigDecimal amount = new BigDecimal(trade.getAmount()).divide(BigDecimal.TEN.pow(feeScale), feeScale, RoundingMode.DOWN);
        // 手续费精度需考虑主币精度
        BigDecimal fee = new BigDecimal(trade.getFee()).divide(BigDecimal.TEN.pow(feeScale), feeScale, RoundingMode.DOWN);
        // TODO 充币商户业务处理

        return true;
    }
}
