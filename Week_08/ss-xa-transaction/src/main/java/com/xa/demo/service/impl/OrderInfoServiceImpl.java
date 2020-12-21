package com.xa.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xa.demo.entity.OrderInfo;
import com.xa.demo.mapper.OrderInfoMapper;
import com.xa.demo.service.OrderInfoService;
import org.springframework.stereotype.Service;


@Service
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements OrderInfoService {
}
