package com.livegoods.feedback.contorller;

import com.livegoods.commons.pojo.Result;
import com.livegoods.feedback.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class FeedbackContorller {
    @Autowired
    private FeedbackService feedbackService;
    @PostMapping("/feelback")
    public Result<Object> feedback(String orderId, String feelback,int rate){
        return feedbackService.feedback(orderId, feelback, rate);
    }
}
