package org.gz.sharding.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.gz.sharding.entity.UserInfo;

import java.util.List;


@Mapper
public interface UserInfoMapper {

    long queryCount();

    int updateUserInfo(@Param("id") int id);

    List<UserInfo> queryAll();

}
