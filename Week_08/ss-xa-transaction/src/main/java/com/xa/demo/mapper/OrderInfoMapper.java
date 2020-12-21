package com.xa.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xa.demo.entity.OrderInfo;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface OrderInfoMapper extends BaseMapper<OrderInfo> {
}
