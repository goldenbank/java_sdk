package com.lola.digiccy.controller;

import com.alibaba.fastjson.JSON;
import com.lola.digiccy.client.ApiResult;
import com.lola.digiccy.client.IPayClient;
import com.lola.digiccy.client.PayClient;
import com.lola.digiccy.constant.TradeConstant;
import com.lola.digiccy.entity.*;
import com.lola.digiccy.entity.Address;
import com.lola.digiccy.request.TopApiRequest;
import com.lola.digiccy.response.TopApiResponse;
import com.lola.digiccy.util.GeneratorUtil;
import com.lola.digiccy.util.HttpUtil;
import com.lola.digiccy.util.TokenCache;
import lombok.Data;
import okhttp3.Response;
import org.apache.catalina.Session;
import org.apache.commons.codec.digest.DigestUtils;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author MECHREVO
 */
@RestController
@RequestMapping(value = "/api", produces = "application/json;charset=utf-8")
public class ApiController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private Map<String, PayClient> payClients = new ConcurrentHashMap<>();

    @RequestMapping(value = "/getToken", method = RequestMethod.POST)
    public ApiResult<String> getToken(@RequestBody Token token, HttpServletRequest request) throws IOException {
        PayClient payClient = new PayClient(token.getMerchantId(), token.getWalletNo(), token.getApiKey(), token.getGatewayUrl(), 1000000000);
        ApiResult<String> tokenResult = payClient.generateToken();
        if (tokenResult.getCode() == 200) {
            System.out.println("η­Ύεεζ" + tokenResult.getData());
            request.getSession().setAttribute("token", tokenResult.getData());
            payClient.setToken(tokenResult.getData());
            // ζε₯Tokenε?δΎ
            payClients.put(tokenResult.getData(), payClient);
        }
        return tokenResult;
    }

    @RequestMapping(value = "/creatAddress", method = RequestMethod.POST)
    public ApiResult<TopApiResponse.Address> creatAddress(@RequestBody Address address, HttpServletRequest request) throws IOException {
        String token = (String) request.getSession().getAttribute("token");
        TopApiRequest.CreateAddressRequest addressRequest = new TopApiRequest.CreateAddressRequest();
        addressRequest.setCoinType(address.getCoinType());
        addressRequest.setCallUrl(address.getCallUrl());
        addressRequest.setAlias(address.getAlias());
        ApiResult<TopApiResponse.Address> result = payClients.get(token).createAddress(addressRequest);
        return result;
    }

    @RequestMapping(value = "/transferMoney", method = RequestMethod.POST)
    public ApiResult<?> transferMoney(@RequestBody Transfer transfer, HttpServletRequest request) throws IOException {
        String token = (String) request.getSession().getAttribute("token");
        TopApiRequest.TransferRequest transferRequest = new TopApiRequest.TransferRequest();
        transferRequest.setSubType(transfer.getSubType());
        transferRequest.setMainType(transfer.getMainType());
        transferRequest.setAddress(transfer.getAddress());
        transferRequest.setAmount(transfer.getAmount());
        transferRequest.setCallUrl(transfer.getCallUrl());
        transferRequest.setCheckUrl(transfer.getCheckUrl());
        transferRequest.setMemo(transfer.getMemo());
        transferRequest.setBlockchainTag(transfer.getBlockchainTag());
        transferRequest.setOuterId(GeneratorUtil.getOrderId("K"));
        ApiResult<?> result = payClients.get(token).transfer(transferRequest);
        return result;
    }

    @RequestMapping(value = "/checkAddress", method = RequestMethod.POST)
    public ApiResult<?> checkAddress(@RequestBody Check check, HttpServletRequest request) throws IOException {
        String token = (String) request.getSession().getAttribute("token");
        TopApiRequest.CheckAddressRequest checkAddressRequest = new TopApiRequest.CheckAddressRequest();
        checkAddressRequest.setAddress(check.getAddress());
        checkAddressRequest.setCoinType(check.getCoinType());
        ApiResult<?> result = payClients.get(token).checkAddress(checkAddressRequest);
        return result;
    }

    @RequestMapping(value = "/oderDetails", method = RequestMethod.POST)
    public ApiResult<TopApiResponse.Order> orderDetails(@RequestBody Order order, HttpServletRequest request) throws IOException {
        String token = (String) request.getSession().getAttribute("token");
        TopApiRequest.OrderRequest orderRequest = new TopApiRequest.OrderRequest();
        orderRequest.setMainType(order.getMainType());
        orderRequest.setSubType(order.getSubType());
        order.setOuterId(order.getOuterId());
        ApiResult<TopApiResponse.Order> result = payClients.get(token).querySingleOrder(orderRequest);
        return result;
    }

    @RequestMapping(value = "/transactionInfo", method = RequestMethod.POST)
    public String  transactionInfo(@RequestBody Transaction transaction, HttpServletRequest request) throws Exception {
        String token = (String) request.getSession().getAttribute("token");
        TopApiResponse.TradeNotify model = new TopApiResponse.TradeNotify();
        TopApiResponse.TradeNotify.TradeData data = new TopApiResponse.TradeNotify.TradeData();
        data.setAddress(transaction.getAddress());
        data.setAmount(transaction.getAmount());
        data.setMemo(transaction.getMemo());
        data.setMainType(transaction.getMainType());
        data.setTradeId(transaction.getTradeId());
        data.setOuterId(transaction.getOuterId());
        data.setSubType(transaction.getSubType());
        data.setBlockHeight(transaction.getBlockHeight());
        data.setPrecisions(transaction.getPrecisions());
        data.setFee(transaction.getFee());
        data.setTradeType(transaction.getTradeType());
        data.setTxId(transaction.getTxId());
        data.setStatus(transaction.getStatus());
        TokenCache.TokenCache.put(transaction.getTradeId(),token);
        model.setTradeData(data);
        String sign = DigestUtils.md5Hex(token + JSON.toJSONString(model.getTradeData())).toLowerCase();
        model.setSign(sign);
        String result = HttpUtil.callBack(transaction.getCallUrl(), model);
        return result;
    }

    @RequestMapping(value = "/strategyInfo", method = RequestMethod.GET)
    public List strategy(HttpServletRequest request) throws IOException {
        String token = (String) request.getSession().getAttribute("token");
        List<TopApiResponse.CoinStrategy> list = new ArrayList<>();
        ApiResult<Map<String, TopApiResponse.CoinStrategy>> data = payClients.get(token).getCoinStrategy();
        Map<String, TopApiResponse.CoinStrategy> map = data.getData();
        if (map != null) {
            for (TopApiResponse.CoinStrategy coinStrategy : map.values()) {
                list.add(coinStrategy);
            }
        }
        return list;
    }

    /**
     * δΊ€ζεθ°ε€ηdemo
     * @param tradeNotify
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/back")
    public ApiResult<?> blackish(@RequestBody TopApiResponse.TradeNotify tradeNotify,HttpServletRequest request) throws Exception {
        //ζ­£εΌη―ε’δΈθ¦θΏθ‘η­Ύειͺθ―
        PayClient payClient=new PayClient();
        payClient.setToken(TokenCache.TokenCache.get(tradeNotify.getTradeData().getTradeId()));
        payClient.setApiKey(TokenCache.TokenCache.get(tradeNotify.getTradeData().getTradeId()));
        if (!payClient.checkSign(JSON.toJSONString(tradeNotify.getTradeData()), payClient.getApiKey(), tradeNotify.getSign())) {
            logger.error("ιͺη­Ύε€±θ΄₯,η­ΎεδΏ‘ζ―δΈδΈθ΄,εη­ΎεδΏ‘ζ―γ{}γ", tradeNotify.getSign());
            return ApiResult.error();
        }
        //ζεΈθ―·ζ±
        if (tradeNotify.getTradeData().getTradeType() == TradeConstant.TradeType.WITHDRAW) {
            return  ApiResult.success(dealWithdraw(tradeNotify.getTradeData()));
        }
        //εεΌε€η
        if (tradeNotify.getTradeData().getTradeType() == TradeConstant.TradeType.RECHARGE) {
          return ApiResult.success(dealRecharge(tradeNotify.getTradeData())) ;
        }
        return ApiResult.success();
    }

    /**
     * ζεΈε€η
     *
     * @param trade
     * @return
     */
    private Boolean dealWithdraw(TopApiResponse.TradeNotify.TradeData trade) {
        // εΈη§η²ΎεΊ¦ οΌζη»­θ΄Ήη²ΎεΊ¦ιθθδΈ»εΈη²ΎεΊ¦οΌ
        int feeScale = trade.getPrecisions();
        BigDecimal amount = new BigDecimal(trade.getAmount()).divide(BigDecimal.TEN.pow(feeScale), feeScale, RoundingMode.DOWN);
        // ζη»­θ΄Ήη²ΎεΊ¦ιθθδΈ»εΈη²ΎεΊ¦
        //TODO ιθΏδΈ»εΈη²ΎεΊ¦θ·ε
        // BigDecimal fee = new BigDecimal(trade.getFee()).divide(BigDecimal.TEN.pow(δΈ»εΈη²ΎεΊ¦), δΈ»εΈη²ΎεΊ¦, RoundingMode.DOWN);
        switch (trade.getStatus()) {
            case TradeConstant.WithdrawStatus.TRANSFERING: {
                logger.info("δΈε‘ηΌε·[{}],ε°ε[{}],ιι’[{}],ηΆζ[{}]ε?‘ζ ΈιθΏ", trade.getOuterId(), trade.getAddress(), trade.getAmount(), trade.getStatus());
                // TODO ε?‘ζ ΈιθΏ εζ·δΈε‘ε€η
                break;
            }
            case TradeConstant.WithdrawStatus.REJECT: {
                logger.warn("δΈε‘ηΌε·[{}],ε°ε[{}],ιι’[{}],ηΆζ[{}]ε?‘ζ Έι©³ε", trade.getOuterId(), trade.getAddress(), trade.getAmount(), trade.getStatus());
                // TODO ε?‘ζ Έι©³ε εζ·δΈε‘ε€η
                break;
            }
            case TradeConstant.WithdrawStatus.COMPLETED: {
                logger.error("δΊ€ζθ?°ε½[{}],δΈε‘ηΌε·[{}],ε°ε[{}],ιι’[{}],ηΆζ[{}]δΊ€ζε?ζ", trade.getTxId(), trade.getOuterId(), trade.getAddress(), trade.getAmount(), trade.getStatus());
                // TODO δΊ€ζε?ζ εζ·δΈε‘ε€η
                break;
            }
            case TradeConstant.WithdrawStatus.FAILED: {
                logger.error("δΊ€ζθ?°ε½[{}],δΈε‘ηΌε·[{}],ε°ε[{}],ιι’[{}],ηΆζ[{}]δΊ€ζε€±θ΄₯", trade.getTxId(), trade.getOuterId(), trade.getAddress(), trade.getAmount(), trade.getStatus());
                // TODO δΊ€ζε€±θ΄₯ εζ·δΈε‘ε€η
                break;
            }
            default:
                logger.warn("ζͺε€ηηΆζγ{}γ", trade.getStatus());
                return false;
        }
        return true;
    }

    /**
     * εεΈε€η
     *
     * @param trade
     * @return
     */
    private Boolean dealRecharge(TopApiResponse.TradeNotify.TradeData trade) {
        int feeScale = trade.getPrecisions();
        BigDecimal amount = new BigDecimal(trade.getAmount()).divide(BigDecimal.TEN.pow(feeScale), feeScale, RoundingMode.DOWN);
        // ζη»­θ΄Ήη²ΎεΊ¦ιθθδΈ»εΈη²ΎεΊ¦
        //TODO ιθΏδΈ»εΈη²ΎεΊ¦θ·ε
       //BigDecimal fee = new BigDecimal(trade.getFee()).divide(BigDecimal.TEN.pow(feeScale), feeScale, RoundingMode.DOWN);
        switch (trade.getStatus()) {

            case TradeConstant.WithdrawStatus.COMPLETED: {
                logger.error("δΊ€ζθ?°ε½[{}],δΈε‘ηΌε·[{}],ε°ε[{}],ιι’[{}],ηΆζ[{}]δΊ€ζε?ζ", trade.getTxId(), trade.getOuterId(), trade.getAddress(), trade.getAmount(), trade.getStatus());
                // TODO δΊ€ζε?ζ εζ·δΈε‘ε€η
                break;
            }

            default:
                logger.warn("ζͺε€ηηΆζγ{}γ", trade.getStatus());
                return false;
        }
        return true;
    }
}
