package com.atguigu.cloud.mygateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * @auther zzyy
 * @create 2023-11-22 17:41
 */

@Component //不要忘记
@Slf4j
public class MyGlobalFilter implements GlobalFilter, Ordered
{

    public static final String BEGIN_VISIT_TIME = "begin_visit_time";//开始访问的时间

    /**
     * 自定义全局过滤器
     * @param exchange
     * @param chain
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain)
    {
        //1.先记录下开始访问的时间
        exchange.getAttributes().put(BEGIN_VISIT_TIME, System.currentTimeMillis());
        //getAttributes()：这个方法返回一个 Map，用于存储与当前请求相关的属性。
        // 你可以将任意类型的数据放入这个 Map 中，以便在处理请求时进行访问。

        //2.返回统计的各个结果返回给后台
        return chain.filter(exchange).then(

            Mono.fromRunnable(() -> {//Mono 是 Reactor 框架中的一个类，表示一个可能会在未来某个时候发出单个值或者错误的异步操作。
                //Mono.fromRunnable() 是一个静态方法，用于创建一个 Mono，该 Mono 不会发出任何值

                Long beginVisitTime  = exchange.getAttribute(BEGIN_VISIT_TIME);//exchange.getAttributes() 获取开始访问的时间
                if (beginVisitTime != null){
                    log.info("访问接口主机:"+exchange.getRequest().getURI().getHost());
                    log.info("访问接口端口:"+exchange.getRequest().getURI().getPort());
                    log.info("访问接口URL:"+exchange.getRequest().getURI().getPath());
                    log.info("访问接口参数:"+exchange.getRequest().getURI().getRawQuery());
                    log.info("访问接口耗时:"+ (System.currentTimeMillis() - beginVisitTime) +"ms");
                    log.info("============================================");
                    System.out.println();
                }

            })
            //不用lambda表达式的写法
            //Mono.fromRunnable(new Runnable() {
            //    @Override
            //    public void run() {
            //     }
            //});
        );
    }

    /**
     * 数字越小，优先级越高
     * @return
     */
    @Override
    public int getOrder()
    {
        return 0;
    }
}























