package com.livegoods.feedback.service.impl;

import com.codingapi.txlcn.tc.annotation.TccTransaction;
import com.livegoods.commons.pojo.Result;
import com.livegoods.feedback.dao.FeedbackDao;
import com.livegoods.feedback.openfeign.OrderServiceInterface;
import com.livegoods.feedback.service.FeedbackService;
import com.livegoods.pojo.Comments;
import com.livegoods.pojo.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class  FeedbackServiceImpl implements FeedbackService {
    @Autowired
    private FeedbackDao feedbackDao;
    @Autowired
    private OrderServiceInterface orderServiceInterface;

    @Override
    @TccTransaction
    public Result<Object> feedback(String orderId, String feelback, int rate) {
        Result<Object> result = new Result<>();
        Order order = orderServiceInterface.getOrderById(orderId);
        if(order == null){
            result.setMsg("评论失败");
            result.setStatus(500);
        }
        Comments comments = new Comments();
        comments.setUsername(order.getUser());
        comments.setComment(feelback);
        comments.setHouseId(order.getHouseId());
        comments.setStar(rate);
        //保存评论的同时还要更新订单状态
        feedbackDao.saveFeedback(comments);
        result.setStatus(200);
        result.setMsg("评论成功");
        //更新订单状态
        orderServiceInterface.modifyOrderCommentState(order.getId());
        return result;
    }

    public Result<Object> comfirmFfeedback(String orderId, String feelback, int rate){
        Result<Object> result = new Result<>();
        result.setStatus(200);
        result.setMsg("评论成功");

        return result;
    }

    public Result<Object> cancelFfeedback(String orderId, String feelback, int rate){
        Order order = orderServiceInterface.getOrderById(orderId);
        Comments comments = new Comments();
        comments.setStar(rate);
        comments.setHouseId(order.getHouseId());
        comments.setComment(feelback);
        comments.setUsername(order.getUser());
        feedbackDao.removeByComments(comments);
        Result<Object> result = new Result<>();
        result.setStatus(500);
        result.setMsg("评论失败");
        return result;
    }


}
