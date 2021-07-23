package com.lola.digiccy.conf;

import com.alibaba.fastjson.JSONObject;
import com.lola.digiccy.client.ApiResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * @author MECHREVO
 */
public class Handle extends HandlerInterceptorAdapter {
    private String contextPath;
    public Handle(String contextPath){
        this.contextPath=contextPath;
    }
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
       if(request.getSession().getAttribute("token")==null){
           if(request.getRequestURI().toString().contains("api")) {
               ajaxReturn(response, new ApiResult<>(4000, "TOKEN_IS_NULL"));
           }else {
               response.sendRedirect(contextPath);
           }
          return false;
       }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);
    }

    @Override
    public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        super.afterConcurrentHandlingStarted(request, response, handler);
    }

    public void ajaxReturn(HttpServletResponse response, ApiResult<?> apiResult) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/json; charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.print(JSONObject.toJSONString(apiResult));
        out.flush();
        out.close();
    }
}
