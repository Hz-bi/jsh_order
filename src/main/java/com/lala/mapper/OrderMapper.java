package com.lala.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lala.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 订单表 Mapper 接口
 * </p>
 *
 * @author jsh
 * @since 2023-04-22
 */
@Mapper
public interface OrderMapper extends BaseMapper<Order> {


}
