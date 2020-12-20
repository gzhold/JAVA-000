package com.hmily.demo.dubboconsumer;


import org.apache.dubbo.config.annotation.DubboReference;
import com.demo.common.dubboapi.service.AccountService;
import com.demo.common.dubboapi.vo.AccountDTO;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;

@SpringBootApplication
public class DubboConsumerApplication {

    @DubboReference(version = "1.0.0", url = "dubbo://127.0.0.1:55551")
    private AccountService accountService;

    public static void main(String[] args) {

        SpringApplication.run(DubboConsumerApplication.class).close();


    }

    @Bean
    public ApplicationRunner runner() {
        return args -> {
            AccountDTO dto = new AccountDTO();
            dto.setUserId("20000");
            dto.setAmount(new BigDecimal(100));
            dto.setUnit("DOLLAR");
            boolean confirm = accountService.exchange(dto);
        };
    }
}
