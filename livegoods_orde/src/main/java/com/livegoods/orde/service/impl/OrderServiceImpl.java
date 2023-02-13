package com.livegoods.orde.service.impl;

import com.codingapi.txlcn.tc.annotation.TccTransaction;
import com.livegoods.commons.pojo.Result;
import com.livegoods.orde.dao.OrderDao;
import com.livegoods.orde.service.OrderService;
import com.livegoods.pojo.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderDao orderDao;
    @Override
    @TccTransaction
    public Result<Order> addOrder(Order order) {
        orderDao.saveOrder(order);
        Result<Order> result = new Result<>();
        result.setResults(Arrays.asList(order));
        result.setStatus(200);
        return result;
    }

    @Override
    public List<Order> getOrdersByUser(String user) {
        return orderDao.findOrdersByUser(user);
    }

    @Override
    public Order getOrdersById(String id) {
        return orderDao.findOrdersById(id);
    }

    @Override
    @TccTransaction
    public Result<Object> modifyOrderCommentState(String orderId) {
        orderDao.modifyOrderCommentState(orderId,2);
        Result<Object> result = new Result<>();
        result.setStatus(200);
        return result;
    }
    public Result<Object> confirmModifyOrderCommentState(String orderId) {
        Result<Object> result = new Result<>();
        result.setStatus(200);
        return result;
    }

    public Result<Object> cancelModifyOrderCommentState(String orderId) {
        //恢复订单评论状态， 0：未评论 2：已评论
        orderDao.modifyOrderCommentState(orderId,0);
        Result<Object> result = new Result<>();
        result.setStatus(500);
        return result;
    }

    // 确定
    public Result<Order> confirmAddOrder(Order order){
        Result<Order> result = new Result<>();
        result.setResults(Arrays.asList(order));
        result.setStatus(200);
        return result;
    }
    // 取消,需要回滚事务，删除刚新增的数据
    public Result<Order> cancelAddOrder(Order order){
        Result<Order> result = new Result<>();
        result.setStatus(500);
        orderDao.deleteOrder(order);
        return result;
    }

}
