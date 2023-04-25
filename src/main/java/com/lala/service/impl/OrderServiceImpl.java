package com.lala.service.impl;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONException;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lala.constant.Constants;
import com.lala.entity.Order;
import com.lala.exception.BusinessRuntimeException;
import com.lala.mapper.OrderMapper;
import com.lala.param.PlaceOrderParam;
import com.lala.param.TakeOrderParam;
import com.lala.service.OrderService;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author jsh
 * @since 2023-04-22
 */
@Service
@Component
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {


    @Value("${webapi.apikey}")
    private String apikey;


    @Transactional
    @Override
    public ResponseEntity<?> placeOrder(PlaceOrderParam placeOrderParam) {

        List<String> origin = placeOrderParam.getOrigin();

        List<String> destination = placeOrderParam.getDestination();

        if (origin == null || destination == null || origin.size() != 2 || destination.size() != 2) {
            // 处理非法参数
            throw new BusinessRuntimeException("起点或终点经纬度信息有误", HttpStatus.BAD_REQUEST);
        }

        double startLat, startLng, endLat, endLng;
        try {
            startLat = Double.parseDouble(origin.get(0));
            startLng = Double.parseDouble(origin.get(1));
            endLat = Double.parseDouble(destination.get(0));
            endLng = Double.parseDouble(destination.get(1));
        } catch (NumberFormatException e) {
            // 处理非法参数
            throw new BusinessRuntimeException("起点或终点经纬度信息有误", HttpStatus.BAD_REQUEST);
        }

        int distance = getDistance(startLat, startLng, endLat, endLng, apikey);

        Order order = new Order();
        order.setDistance(distance);
        order.setStatus(Constants.ORDER_UNASSIGNED);
        this.save(order);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Http", String.valueOf(HttpStatus.OK.value()));

        return ResponseEntity.ok().headers(headers).body(order);
    }

    @Override
    public ResponseEntity<?> findOrders(Integer page, Integer limit) {

        if (page < 1 || limit < 1) {
            // 处理非法参数
            throw new BusinessRuntimeException("非法参数", HttpStatus.BAD_REQUEST);
        }
        // 计算查询起始位置
        long offset = (page - 1) * limit;

        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.last("LIMIT " + offset + ", " + limit);

        List<Order> orderList = baseMapper.selectList(wrapper);

        // 查询总记录数并计算总页数
        long total = baseMapper.selectCount(null);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Http", String.valueOf(HttpStatus.OK.value()));

        if ((page - 1) * limit >= total) {
            // 超出范围，返回空列表
            return ResponseEntity.ok().headers(headers).body(Collections.emptyList());
        } else {
            return ResponseEntity.ok().headers(headers).body(orderList);
        }
    }


    @Override
    public ResponseEntity<?> takeOrder(Long orderId, TakeOrderParam takeOrderParam) {

        synchronized (orderId.toString().intern()) {
            OrderService proxy = (OrderService)AopContext.currentProxy();
            return proxy.submitTransaction(orderId, takeOrderParam);
        }
    }

    @Transactional
    public ResponseEntity<?> submitTransaction(Long orderId, TakeOrderParam takeOrderParam) {
        Order order = this.getById(orderId);

        if (order == null) {
            throw new BusinessRuntimeException("订单不存在！", HttpStatus.BAD_REQUEST);
        }
        if (!Constants.ORDER_TAKEN.equals(takeOrderParam.getStatus())) {
            throw new BusinessRuntimeException("参数错误！", HttpStatus.BAD_REQUEST);
        }
        // 查询订单状态
        String status = order.getStatus();

        // 判断是否可以接
        if (!Constants.ORDER_UNASSIGNED.equals(status)) {
            // 此单不可被接
            throw new BusinessRuntimeException("此单不可被接", HttpStatus.FORBIDDEN);
        }
        order.setStatus(Constants.ORDER_SUCCESS);
        takeOrderParam.setStatus(Constants.ORDER_SUCCESS);
        // 更改订单
        this.saveOrUpdate(order);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Http", String.valueOf(HttpStatus.OK.value()));
        return ResponseEntity.ok().headers(headers).body(takeOrderParam);
    }


    public int getDistance(double startLat, double startLng, double endLat, double endLng, String apikey) {
        int distance = 0;
        try {
            // 拼接url
            String url = "https://maps.googleapis.com/maps/api/distancematrix/json?origins="
                    + startLat + "," + startLng + "&destinations=" + endLat + "," + endLng + "&key=" + apikey;
            // 建立连接
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            // 读取返回结果
            InputStream inputStream = connection.getInputStream();
            String response = new Scanner(inputStream).useDelimiter("\\A").next();
            // 解析返回结果
            distance = parseDistance(response);
            return distance;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return distance;
    }

    private int parseDistance(String response) {
        int distance = 0;
        try {
            // 将返回结果转换为json格式
            JSONObject jsonObject = new JSONObject(response);
            JSONArray rows = jsonObject.getJSONArray("rows");
            JSONObject row = rows.getJSONObject(0);
            JSONArray elements = row.getJSONArray("elements");
            JSONObject element = elements.getJSONObject(0);
            JSONObject distanceObj = element.getJSONObject("distance");
            String distanceStr = distanceObj.getStr("value");
            // 将距离转换为米
            distance = Integer.parseInt(distanceStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return distance;
    }


    public ResponseEntity<?> findOrdersTest(Integer page, Integer limit, OrderMapper orderMapper) {

        if (page < 1 || limit < 1) {
            // 处理非法参数
            throw new BusinessRuntimeException("非法参数", HttpStatus.BAD_REQUEST);
        }
        // 计算查询起始位置
        long offset = (page - 1) * limit;

        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.last("LIMIT " + offset + ", " + limit);

        List<Order> orderList = orderMapper.selectList(wrapper);

        // 查询总记录数并计算总页数
        long total = orderMapper.selectCount(null);
        long pages = total % limit == 0 ? total / limit : total / limit + 1;

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(total));
        headers.add("X-Total-Pages", String.valueOf(pages));
        headers.set("Http", String.valueOf(HttpStatus.OK.value()));

        if ((page - 1) * limit >= total) {
            // 超出范围，返回空列表
            return ResponseEntity.ok().headers(headers).body(Collections.emptyList());
        } else {
            return ResponseEntity.ok().headers(headers).body(orderList);
        }
    }

    public ResponseEntity<?> placeOrderTest(PlaceOrderParam placeOrderParam, OrderMapper orderMapper, String apikey) {

        List<String> origin = placeOrderParam.getOrigin();
        List<String> destination = placeOrderParam.getDestination();

        if (origin == null || destination == null || origin.size() != 2 || destination.size() != 2) {
            // 处理非法参数
            throw new BusinessRuntimeException("起点或终点经纬度信息有误", HttpStatus.BAD_REQUEST);
        }

        double startLat, startLng, endLat, endLng;
        try {
            startLat = Double.parseDouble(origin.get(0));
            startLng = Double.parseDouble(origin.get(1));
            endLat = Double.parseDouble(destination.get(0));
            endLng = Double.parseDouble(destination.get(1));
        } catch (NumberFormatException e) {
            // 处理非法参数
            throw new BusinessRuntimeException("起点或终点经纬度信息有误", HttpStatus.BAD_REQUEST);
        }

        int distance = getDistance(startLat, startLng, endLat, endLng, apikey);

        Order order = new Order();
        order.setDistance(distance);
        order.setStatus(Constants.ORDER_UNASSIGNED);
        orderMapper.insert(order);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Http", String.valueOf(HttpStatus.OK.value()));

        return ResponseEntity.ok().headers(headers).body(order);
    }

    public ResponseEntity<?> takeOrderTest(Long orderId, TakeOrderParam takeOrderParam, OrderMapper orderMapper) {

        synchronized (orderId.toString().intern()) {

            return submitTransaction(orderId, takeOrderParam);
        }
    }

}
