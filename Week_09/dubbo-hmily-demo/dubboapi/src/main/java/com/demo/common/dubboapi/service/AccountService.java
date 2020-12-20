package com.demo.common.dubboapi.service;

import com.demo.common.dubboapi.vo.AccountDTO;
import org.dromara.hmily.annotation.Hmily;

public interface AccountService {

    @Hmily
    boolean exchange(AccountDTO accountDTO);
}
