package org.gz.multi.service;


import org.gz.multi.entity.UserInfo;

public interface UserService {

    long queryTotal();

    UserInfo queryUser(Integer id);

}
