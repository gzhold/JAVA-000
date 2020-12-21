package com.xa.demo;

import com.xa.demo.entity.OrderInfo;
import com.xa.demo.service.OrderInfoService;
import com.xa.demo.service.XAService;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@MapperScan("com.xa.demo.mapper")
@SpringBootApplication
public class XADemoMainApp {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(XADemoMainApp.class, args);
        invoke(applicationContext);
    }

    public static void invoke(ConfigurableApplicationContext context) {

        XAService service = context.getBean(XAService.class);
        List<OrderInfo> list = service.addTenOrderInfo();
        log.info("Order_info list: {}", list);

        List<OrderInfo> rollbackUserList = new ArrayList<>();
        try {
            service.addTenOrderInfoWithError(rollbackUserList);
        } catch (Exception e) {
            log.warn("rollback", e);
        }

        OrderInfoService orderService = context.getBean(OrderInfoService.class);
        OrderInfo orderInfo = orderService.getById(rollbackUserList.get(0).getOrder_id());
        log.info("order -- null: {}", orderInfo);
    }

}
