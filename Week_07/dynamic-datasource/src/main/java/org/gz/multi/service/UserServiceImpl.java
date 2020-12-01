package org.gz.multi.service;

import org.gz.multi.config.DataSourceType;
import org.gz.multi.config.MyDataSource;

import org.gz.multi.entity.UserInfo;
import org.gz.multi.mapper.UserInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserInfoMapper userInfoMapper;



    @MyDataSource(value=DataSourceType.SLAVE_1)
    @Override
    public long queryTotal() {
        return userInfoMapper.queryCount();
    }

    @Override
    public UserInfo queryUser(Integer id) {
        assert id != null;
        return userInfoMapper.queryUser(id);
    }
}
