package com.atguigu.cloud.controller;

import com.atguigu.cloud.entities.PayDTO;
import com.atguigu.cloud.resp.ResultData;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@Slf4j
public class OrderController {

//    public static final String PaymentSrv_URL = "http://localhost:8001/";//先硬编码写死
    public static final String PaymentSrv_URL = "http://cloud-payment-service";//服务注册中心上的微服务名字

    @Resource
    private RestTemplate restTemplate;
    //public <T> T postForObject(String url, Object request, Class<T> responseType, Object... uriVariables)
    //String url：要调用的 API 的完整 URL 地址。例如：http://localhost:8001/pay/add。
    //Object request：这是你想要发送的请求体，通常是一个 Java 对象（如 PayDTO）。这个对象会被转换为 JSON 格式发送到服务器。
    //Class<T> responseType：你期望服务器返回的数据类型。通常是你自定义的响应对象，比如 ResultData.class。这个参数告诉 Spring 如何解析 HTTP 响应。
    //Object... uriVariables：可选参数，用于动态替换 URL 中的占位符。比如，如果 URL 中有 {id}，你可以传入 ID 值。

    //public <T> T getForObject(String url, Class<T> responseType, Object... uriVariables)
    //String url：要调用的 API 的完整 URL 地址。例如：http://localhost:8001/pay/get/1。
    //Class<T> responseType：你期望服务器返回的数据类型。比如，ResultData.class。
    //Object... uriVariables：可选参数，用于动态替换 URL 中的占位符。比如，如果 URL 中有 {id}，你可以传入 ID 值。



    @GetMapping("/consumer/pay/add")
    public ResultData addOrder(PayDTO payDTO){
        return restTemplate.postForObject(PaymentSrv_URL+"/pay/add",payDTO,ResultData.class);

    }

    @GetMapping("/consumer/pay/get/{id}")
    public ResultData getPayInfo(@PathVariable("id") Integer id){
        return  restTemplate.getForObject(PaymentSrv_URL+"/pay/get/"+id,ResultData.class,id);

    }

    @GetMapping(value = "/consumer/pay/get/info")
    private String getInfoByConsul()
    {
        return restTemplate.getForObject(PaymentSrv_URL + "/pay/get/info", String.class);
    }

    @Resource
    private DiscoveryClient discoveryClient;
    @GetMapping("/consumer/discovery")
    public String discovery()
    {
        List<String> services = discoveryClient.getServices();
        for (String element : services) {
            System.out.println(element);
        }

        System.out.println("===================================");

        List<ServiceInstance> instances = discoveryClient.getInstances("cloud-payment-service");
        for (ServiceInstance element : instances) {
            System.out.println(element.getServiceId()+"\t"+element.getHost()+"\t"+element.getPort()+"\t"+element.getUri());
        }

        return instances.get(0).getServiceId()+":"+instances.get(0).getPort();
    }


//    @GetMapping("/consumer/pay/update")
//    public ResultData updatePayInfo(PayDTO payDTO){
//        return restTemplate.postForObject(PaymentSrv_URL + "/pay/update", payDTO, ResultData.class);
//    }
//
//    @GetMapping("/consumer/pay/del/{id}")
//    public ResultData delPayInfo(@PathVariable("id") Integer id){
//        return restTemplate.getForObject(PaymentSrv_URL + "/pay/del/" + id, ResultData.class, id);
//    }

}






































