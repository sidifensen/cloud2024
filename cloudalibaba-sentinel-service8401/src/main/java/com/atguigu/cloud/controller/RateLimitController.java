package com.atguigu.cloud.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @auther zzyy
 * @create 2023-05-30 16:13
 */
@RestController
@Slf4j
public class RateLimitController
{

    //1.按照地址限流+默认限流返回
    @GetMapping("/rateLimit/byUrl")
    public String byUrl()
    {
        return "按rest地址限流测试OK";
    }

    //2.按照资源名称限流+自定义限流异常处理
    @GetMapping("/rateLimit/byResource")
    @SentinelResource(value = "byResourceSentinelResource",blockHandler = "handleException")
    public String byResource()//第一个参数是资源名称,第二个参数是blockHandler是自定义限流异常处理方法
    {
        return "按资源名称SentinelResource限流测试OK";
    }
    //自定义限流异常处理方法,不再使用默认
    public String handleException(BlockException exception)
    {
        return "服务不可用@SentinelResource启动"+"\t"+"o(╥﹏╥)o";
    }

    //3.按照资源名称限流+自定义限流异常处理+服务降级处理
    @GetMapping("/rateLimit/doAction/{p1}")
    @SentinelResource(value = "doActionSentinelResource",
            blockHandler = "doActionBlockHandler", fallback = "doActionFallback")
    public String doAction(@PathVariable("p1") Integer p1) {
        if (p1 == 0){
            throw new RuntimeException("p1等于零直接异常");
        }
        return "doAction";
    }
    //blockHandler,主要针对sentinel配置后出现的违规情况处理
    public String doActionBlockHandler(@PathVariable("p1") Integer p1,BlockException e){
        log.error("sentinel配置自定义限流了:{}", e);
        return "sentinel配置自定义限流了";
    }
    //fallback,主要针对程序逻辑异常降级处理
    public String doActionFallback(@PathVariable("p1") Integer p1,Throwable e){
        log.error("程序逻辑异常了:{}", e);
        return "程序逻辑异常了"+"\t"+e.getMessage();
    }

    //热点参数限流
    @GetMapping("/testHotKey")
    @SentinelResource(value = "testHotKey",blockHandler = "dealHandler_testHotKey")
    public String testHotKey(@RequestParam(value = "p1",required = false) String p1,
                             @RequestParam(value = "p2",required = false) String p2){
        return "------testHotKey";
    }
    public String dealHandler_testHotKey(String p1,String p2,BlockException exception)
    {
        return "-----dealHandler_testHotKey";
    }

}






























