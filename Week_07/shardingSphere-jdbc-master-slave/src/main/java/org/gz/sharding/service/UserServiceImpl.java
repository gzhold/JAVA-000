package org.gz.sharding.service;


import org.gz.sharding.entity.UserInfo;
import org.gz.sharding.mapper.UserInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserInfoMapper userMapper;


    @Override
    public long queryTotal() {
        return userMapper.queryCount();
    }

    @Override
    public int updateUserInfo(Integer id) {
        assert null != id;
        return userMapper.updateUserInfo(id);
    }

    @Override
    public List<UserInfo> queryAll() {
        return userMapper.queryAll();
    }
}
