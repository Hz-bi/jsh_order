package com.lala;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lala.entity.Order;
import com.lala.mapper.OrderMapper;
import com.lala.param.PlaceOrderParam;
import com.lala.param.TakeOrderParam;
import com.lala.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

@Component
@SpringBootTest
public class UnitTest extends ServiceImpl<OrderMapper, Order> {

//    @Autowired
//    private OrderMapper orderMapper;
//
//
//    private final OrderServiceImpl orderService = new OrderServiceImpl();
//
//
//    @Value("${webapi.apikey}")
//    private String apikey;
//
//    @Test
//    public void placeOrder_withValidParam_returnsOkResponse() {
//        // 准备测试数据
//        List<String> origin = Arrays.asList("31.2293", "121.4651");
//        List<String> destination = Arrays.asList("31.2389", "121.4992");
//        PlaceOrderParam param = new PlaceOrderParam(origin, destination);
//
//        // 执行测试
//        ResponseEntity<?> responseEntity = orderService.placeOrderTest(param, orderMapper, apikey);
//        HttpHeaders headers = responseEntity.getHeaders();
//
//        // 验证结果
//        assertEquals(String.valueOf(HttpStatus.OK.value()), headers.getFirst("Http"));
//
//    }
//
//
//    @Test
//    public void findOrders_withValidParam_returnsOkResponse() {
//        // 准备测试数据
//        Integer page = 2;
//        Integer limit = 2;
//
//
//        // 执行测试
//        ResponseEntity<?> responseEntity = orderService.findOrdersTest(page, limit, orderMapper);
//        HttpHeaders headers = responseEntity.getHeaders();
//
//        // 验证结果
//        assertEquals(String.valueOf(HttpStatus.OK.value()), headers.getFirst("Http"));
//
//    }
//
//
//    @Test
//    public void takeOrder_withValidParam_returnsOkResponse() {
//        // 准备测试数据
//        Long id = 1650456380895215618L;
//        TakeOrderParam takeOrderParam = new TakeOrderParam();
//        takeOrderParam.setStatus("TAKEN");
//
//        // 执行测试
//        ResponseEntity<?> responseEntity = orderService.takeOrderTest(id, takeOrderParam, orderMapper);
//        HttpHeaders headers = responseEntity.getHeaders();
//
//        // 验证结果
//        assertEquals(HttpStatus.OK.toString(), headers.getFirst("Http"));
//
//    }


}
