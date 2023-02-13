package com.livegoods.orde.dao.impl;

import com.livegoods.commons.pojo.Result;
import com.livegoods.orde.dao.OrderDao;
import com.livegoods.pojo.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderDaoImpl implements OrderDao {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Override
    public void saveOrder(Order order) {
        mongoTemplate.save(order);
    }

    @Override
    public void deleteOrder(Order order) {
        mongoTemplate.remove(order);
    }

    @Override
    public List<Order> findOrdersByUser(String user) {
        Query query = Query.query(
                Criteria.where("user").is(user)
        );
        return mongoTemplate.find(query, Order.class);
    }

    @Override
    public Order findOrdersById(String id) {
        return mongoTemplate.findById(id, Order.class);
    }

    @Override
    public void modifyOrderCommentState(String orderId, int i) {
        Query query = Query.query(
                Criteria.where("_id").is(orderId)
        );
        Update update = Update.update("commentState" , i);
        mongoTemplate.updateFirst(query, update, Order.class);
    }
}
