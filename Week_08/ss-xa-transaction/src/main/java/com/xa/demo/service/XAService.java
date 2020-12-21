package com.xa.demo.service;

import com.xa.demo.entity.OrderInfo;
import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Component
public class XAService {

    @Resource
    private OrderInfoService orderService;

    @ShardingTransactionType(TransactionType.XA)
    @Transactional
    public List<OrderInfo> addTenOrderInfo() {
        List<OrderInfo> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setOrder_id("12312312"+i);
            orderInfo.setGood_id("1212112"+i);
            orderInfo.setUser_id("12324312"+i);
            orderInfo.setCreate_time(LocalDateTime.now());
            orderInfo.setOrder_number("5");
            orderInfo.setOrder_status(1);
            orderInfo.setPay_time(LocalDateTime.now());
            orderInfo.setPayaccno("6262323642326323");
            orderInfo.setReal_amount(new BigDecimal(55));
            orderInfo.setRemark("test1");
            orderInfo.setTrack_no("6262323641234223");
            orderInfo.setUpdate_time(LocalDateTime.now());
            orderService.save(orderInfo);
            list.add(orderInfo);
        }
        return list;
    }

    @ShardingTransactionType(TransactionType.XA)
    @Transactional
    public void addTenOrderInfoWithError(List<OrderInfo> list) {
        for (int i = 0; i < 10; i++) {
            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setOrder_id("12312312"+i);
            orderInfo.setGood_id("1212112"+i);
            orderInfo.setUser_id("12324312"+i);
            orderInfo.setOrder_number("5");
            orderInfo.setCreate_time(LocalDateTime.now());
            orderInfo.setOrder_status(1);
            orderInfo.setPay_time(LocalDateTime.now());
            orderInfo.setPayaccno("6262323642326323");
            orderInfo.setReal_amount(new BigDecimal(55));
            orderInfo.setRemark("test1");
            orderInfo.setUpdate_time(LocalDateTime.now());
            orderInfo.setTrack_no("6262323641234223");
            if (i == 9) {
                throw new RuntimeException("test xa transaction.");
            }
            list.add(orderInfo);
        }
    }

}
