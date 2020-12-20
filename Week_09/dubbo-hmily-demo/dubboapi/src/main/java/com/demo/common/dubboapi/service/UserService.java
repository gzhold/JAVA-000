package com.demo.common.dubboapi.service;

import com.demo.common.dubboapi.bean.User;

public interface UserService {

    User findById(int id);
}
