package com.demo.common.dubboapi.service;

import com.demo.common.dubboapi.bean.Order;

public interface OrderService {

    Order findOrderById(int id);
}
