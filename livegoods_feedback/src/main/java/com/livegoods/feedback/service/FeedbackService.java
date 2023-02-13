package com.livegoods.feedback.service;

import com.livegoods.commons.pojo.Result;

public interface FeedbackService {
    Result<Object> feedback(String orderId, String feelback, int rate);
}
