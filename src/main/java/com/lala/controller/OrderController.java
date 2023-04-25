package com.lala.controller;

import com.lala.param.PlaceOrderParam;
import com.lala.param.TakeOrderParam;
import com.lala.service.OrderService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import static org.springframework.http.ResponseEntity.ok;

/**
 * <p>
 * 订单表 前端控制器
 * </p>
 *
 * @author jsh
 * @since 2023-04-22
 */
@RestController
@RequestMapping("")
public class OrderController {

    @Autowired
    OrderService orderService;

    @ApiOperation(value = "用户下单", notes = "用户下单")
    @PostMapping(value = "/orders")
    public ResponseEntity<?> placeOrder(@RequestBody PlaceOrderParam placeOrderParam) {

        return ok(orderService.placeOrder(placeOrderParam));
    }

    @ApiOperation(value = "查单", notes = "查单")
    @GetMapping(value = "/orders")
    public ResponseEntity<?> findOrders(@RequestParam("page") Integer page, @RequestParam("limit") Integer limit) {

        return ok(orderService.findOrders(page, limit));
    }

    @ApiOperation(value = "司机抢单", notes = "司机抢单")
    @PatchMapping(value = "/orders/{id}")
    public ResponseEntity<?> updateOrder(@PathVariable("id") Long orderId, @RequestBody TakeOrderParam takeOrderParam)  {

        return ok(orderService.takeOrder(orderId, takeOrderParam));
    }


}
