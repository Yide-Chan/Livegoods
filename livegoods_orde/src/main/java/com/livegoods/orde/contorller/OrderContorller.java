package com.livegoods.orde.contorller;

import com.livegoods.commons.pojo.Result;
import com.livegoods.orde.service.OrderService;
import com.livegoods.pojo.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class OrderContorller {
    @Autowired
    private OrderService orderService;
    @PostMapping("/addOrder")
    public Result<Order> addOrder(@RequestBody Order order){
        return orderService.addOrder(order);
    }
    @GetMapping("/order")
    public List<Order> getOrdersByUser(String user){
        return orderService.getOrdersByUser(user);
    }
    @GetMapping("getOrderById")
    public Order getOrderById(String id){
        return orderService.getOrdersById(id);
    }
    @PostMapping("/modifyOrderCommentState")
    public Result<Object> modifyOrderCommentState(String orderId){
        return orderService.modifyOrderCommentState(orderId);
    }
}
