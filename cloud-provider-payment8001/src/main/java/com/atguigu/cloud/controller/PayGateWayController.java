package com.atguigu.cloud.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.atguigu.cloud.entities.Pay;
import com.atguigu.cloud.resp.ResultData;
import com.atguigu.cloud.service.PayService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

/**
 * @auther zzyy
 * @create 2023-11-20 13:09
 */
@RestController
public class PayGateWayController{
   //Nginx更适用于通用的Web服务和反向代理，而Gateway更适用于微服务架构中的请求管理和集中控制

    @Resource
    PayService payService;

    @GetMapping(value = "/pay/gateway/get/{id}")
    public ResultData<Pay> getById(@PathVariable("id") Integer id)
    {
        Pay pay = payService.getById(id);
        return ResultData.success(pay);
    }

    @GetMapping(value = "/pay/gateway/info")
    public ResultData<String> getGatewayInfo()
    {
        return ResultData.success("gateway info test："+ IdUtil.simpleUUID());
    }

    @GetMapping(value = "/pay/gateway/filter")
    public ResultData<String> getGatewayFilter(HttpServletRequest request)
    {
        String result = "";
        Enumeration<String> headers = request.getHeaderNames();//接口中定义了一些方法，通过这些方法可以遍历集合中的元素。
        while(headers.hasMoreElements())
        {
            String headName = headers.nextElement();
            String headValue = request.getHeader(headName);
            System.out.println("请求头名: " + headName +"\t\t\t"+"请求头值: " + headValue);

            if(headName.equalsIgnoreCase("X-Request-atguigu1")
                    || headName.equalsIgnoreCase("X-Request-atguigu2")) {

                result = result+headName + "\t " + headValue +" ";

            }
        }


        System.out.println("=============================================");
        String customerId = request.getParameter("customerId");
        System.out.println("request Parameter customerId: "+customerId);

        String customerName = request.getParameter("customerName");
        System.out.println("request Parameter customerName: "+customerName);
        System.out.println("=============================================");

        return ResultData.success("getGatewayFilter 过滤器 test： "+result+" \t "+ DateUtil.now());
//        DateUtil是hutool提供的一个日期处理工具类，可以方便的处理日期相关的操作。
    }



}


























