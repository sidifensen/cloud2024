package com.atguigu.cloud.mygateway;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
import org.springframework.cloud.gateway.handler.predicate.GatewayPredicate;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ServerWebExchange;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

//需求说明: 自定义配置会员等级userTpye,按照 钻diamond/金glod/银 和yml配置的会员等级,以适配是否可以访问
//自定义断言工厂类,继承AbstractRoutePredicateFactory,并指定Config类
// 注意自定义名字必须以RoutePredicateFactory结尾 RoutePredicateFactory前面的就是写在配置yml中的名字
@Component
public class MyRoutePredicateFactory extends AbstractRoutePredicateFactory<MyRoutePredicateFactory.Config> {

    //这个Config类就是我们的路由断言规则,重要
    //@Validated 注解确保 Config 类中的属性在被使用之前会经过验证，提升了代码的健壮性和错误处理能力。
    // 通过这种方式，可以确保在路由断言的配置中，用户类型总是被正确设定。
    @Validated//在使用该类的实例时，Spring 会自动检查其属性是否符合该属性上声明的约束条件。
    public static class Config {

        @Setter@Getter@NotEmpty
        private String userType;  //钻/金glod/银和yml配置的会员等级

    }

    //短促方式,让yml配置可以用My来代替
    public List<String> shortcutFieldOrder() {
        return Collections.singletonList("userType");
    }

    public MyRoutePredicateFactory() {
        super(MyRoutePredicateFactory.Config.class);
    }

    @Override
    public Predicate<ServerWebExchange> apply(MyRoutePredicateFactory.Config config) {
        return new Predicate<ServerWebExchange>() {
            @Override
            public boolean test(ServerWebExchange serverWebExchange) {

                //检查request的参数里面,uesrType为配置的值,符合配置就通过
                //http://localhost:9527/pay/gateway/get/1?userType=diamond
                String userType = serverWebExchange.getRequest().getQueryParams().getFirst("userType");//获取请求参数中的userType
                if (userType == null) {
                    return false;
                }
                //如果说参数存在,就和config的数据进行比较
                if (userType.equalsIgnoreCase(config.getUserType())){
                    return true;
                }

                return false;
            }

        };
    }


}









































