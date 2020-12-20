package com.demo.hmily.dubboprovider;

import com.demo.common.dubboapi.bean.User;
import com.demo.common.dubboapi.service.UserService;
import org.apache.dubbo.config.annotation.DubboService;


@DubboService(version = "1.0.0")
public class UserServiceImpl implements UserService {

    @Override
    public User findById(int id) {
        return new User(id, "cuicui" + System.currentTimeMillis());
    }
}
