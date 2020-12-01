package org.gz.multi.mapper;

import org.gz.multi.entity.UserInfo;

public interface UserInfoMapper {

    long queryCount();

    UserInfo queryUser(int id);

}
