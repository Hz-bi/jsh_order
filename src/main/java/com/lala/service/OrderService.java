package com.lala.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lala.entity.Order;
import com.lala.param.PlaceOrderParam;
import com.lala.param.TakeOrderParam;
import org.springframework.http.ResponseEntity;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author jsh
 * @since 2023-04-22
 */
public interface OrderService extends IService<Order> {


    ResponseEntity<?> placeOrder(PlaceOrderParam placeOrderParam);

    ResponseEntity<?> findOrders(Integer page, Integer limit);

    ResponseEntity<?> takeOrder(Long orderId, TakeOrderParam takeOrderParam);

    ResponseEntity<?> submitTransaction(Long orderId, TakeOrderParam takeOrderParam);
}
