package org.gz.sharding.service;

import org.gz.sharding.entity.UserInfo;

import java.util.List;

public interface UserService {

    long queryTotal();

    int updateUserInfo(Integer id);

    List<UserInfo> queryAll();

}
