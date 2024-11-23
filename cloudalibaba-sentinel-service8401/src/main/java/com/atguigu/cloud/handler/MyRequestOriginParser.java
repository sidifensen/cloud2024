package com.atguigu.cloud.handler;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.RequestOriginParser;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

/**
 * @auther zzyy
 * @create 2023-11-30 19:33
 */
//授权规则
@Component
public class MyRequestOriginParser implements RequestOriginParser //自定义请求来源解析器
{
    @Override
    public String parseOrigin(HttpServletRequest httpServletRequest) {//自定义解析请求来源
        return httpServletRequest.getParameter("serverName");//从请求参数中获取serverName
    }
}
























