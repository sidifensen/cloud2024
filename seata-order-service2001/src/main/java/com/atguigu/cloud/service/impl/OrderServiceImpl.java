package com.atguigu.cloud.service.impl;

import com.atguigu.cloud.apis.AccountFeignApi;
import com.atguigu.cloud.apis.StorageFeignApi;
import com.atguigu.cloud.entities.Order;
import com.atguigu.cloud.mapper.OrderMapper;
import com.atguigu.cloud.service.OrderService;
import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderMapper orderMapper;

    @Resource//订单微服务通过OpenFeign去调用库存微服务
    private StorageFeignApi storageFeignApi;

    @Resource//订单微服务通过OpenFeign去调用账户微服务
    private AccountFeignApi accountFeignApi;

    @Override
    @GlobalTransactional(name="zzyy-create-order",rollbackFor=Exception.class) //开启分布式事务
    public void create(Order order) {

        //xid 全局事务id的检查,重要
        String xid = RootContext.getXID();
        //1.新建订单
        log.info("-----------新建订单:"+" xid="+xid);

        //订单新建时默认初始的订单状态为0,创建中
        order.setStatus(0);
        int result = orderMapper.insertSelective(order);

        //插入订单成功后获得插入mysql的实体对象
        Order orderFromDB = null;

        if (result>0){
            //2.从mysql里面查出刚插入的记录
            orderFromDB = orderMapper.selectOne(order);
            log.info("-----------新建订单成功:"+" orderFromDB="+orderFromDB.toString());

            //3.扣减库存
            log.info("-----------开始扣减库存");
            storageFeignApi.decrease(orderFromDB.getProductId(),orderFromDB.getCount());
            log.info("-----------库存扣减成功");

            //4.扣减账户余额
            log.info("-----------开始扣减账户余额");
            accountFeignApi.decrease(orderFromDB.getUserId(),orderFromDB.getMoney());
            log.info("-----------账户余额扣减成功");

            //5.更新订单状态为1,已完成
            log.info("-----------修改订单状态");
            orderFromDB.setStatus(1);

            Example whereCondition=new Example(Order.class);
            Example.Criteria criteria=whereCondition.createCriteria();
            criteria.andEqualTo("userId",orderFromDB.getUserId());
            criteria.andEqualTo("status",0);

            int updateResult = orderMapper.updateByExampleSelective(orderFromDB, whereCondition);
            log.info("-----------订单状态修改成功,updateResult="+updateResult);
            log.info("-----------oderFromDB="+orderFromDB.toString());


        }

        System.out.println();
        log.info("-----------结束订单:"+" xid="+xid);
    }

}


































