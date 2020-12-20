package com.demo.hmily.dubboprovider;

import com.demo.common.dubboapi.bean.Order;
import com.demo.common.dubboapi.service.OrderService;
import org.apache.dubbo.config.annotation.DubboService;


@DubboService(version = "1.0.0")
public class OrderServiceImpl implements OrderService {

    @Override
    public Order findOrderById(int id) {
        return new Order(id, "Cuijing" + System.currentTimeMillis(), 9.9f);
    }
}
