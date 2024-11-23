package com.atguigu.cloud.mygateway;

import lombok.Getter;
import lombok.Setter;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Component
public class MyGatewayFilterFactory extends AbstractGatewayFilterFactory<MyGatewayFilterFactory.Config>
{

    public static class Config {
        @Setter
        @Getter
        private String status;
    }

    @Override
    public GatewayFilter apply(MyGatewayFilterFactory.Config config)
    {
        return new GatewayFilter() {
            @Override//ServerWebExchange是表示一个请求-响应交换的上下文。它封装了 HTTP 请求和响应的信息
            public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
                ServerHttpRequest request =  exchange.getRequest();//获取请求状态
                System.out.println("进入自定义网关过滤器MyGatewayFilterFactory，status===="+config.getStatus());
                if(request.getQueryParams().containsKey("atguigu")) {//判断请求是否包含atguigu参数
                    return chain.filter(exchange);//放行请求
                }else {
                    exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);//给客户端返回非法请求
                    return exchange.getResponse().setComplete();//setComplete()方法表示响应结束，后续不会再有响应数据返回
                }
            }
        };
    }

    public MyGatewayFilterFactory() {
        super(MyGatewayFilterFactory.Config.class);
    }

    //简化过滤器配置
    @Override
    public List<String> shortcutFieldOrder() {
        List<String> list = new ArrayList<String>();
        list.add("status");
        return list;
    }


}






























