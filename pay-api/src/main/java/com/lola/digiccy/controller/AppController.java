package com.lola.digiccy.controller;

import com.lola.digiccy.client.ApiResult;
import com.lola.digiccy.client.IPayClient;
import com.lola.digiccy.response.TopApiResponse;
import com.lola.digiccy.util.StrUtil;
import com.lola.digiccy.entity.Transaction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author MECHREVO
 */
@Controller
@RequestMapping("/")
public class AppController {
    private IPayClient payClient;
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public ModelAndView view() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        return modelAndView;
    }

    @RequestMapping(value = "/address", method = RequestMethod.GET)
    public ModelAndView address( HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("address");
        return modelAndView;
    }
    @RequestMapping(value = "/transfer", method = RequestMethod.GET)
    public ModelAndView transfer(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("transfer");
        return modelAndView;
    }
    @RequestMapping(value = "/check", method = RequestMethod.GET)
    public ModelAndView check(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("check");
        return modelAndView;
    }

    @RequestMapping(value = "/order", method = RequestMethod.GET)
    public ModelAndView order(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("order");
        return modelAndView;
    }
    @RequestMapping(value = "/transaction", method = RequestMethod.GET)
    public ModelAndView trans(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        String token = (String) request.getSession().getAttribute("token");
        String TradeId=StrUtil.getRandomString(64);
        String TxId="w"+System.currentTimeMillis();
        Transaction transaction=new Transaction();
        transaction.setTxId(TxId);
        transaction.setTradeId(TradeId);
        modelAndView.setViewName("transaction");
        modelAndView.addObject("transaction",transaction);
        return modelAndView;
    }
    @RequestMapping(value = "/strategy", method = RequestMethod.GET)
    public ModelAndView strategy(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("strategy");
        return modelAndView;
    }
}