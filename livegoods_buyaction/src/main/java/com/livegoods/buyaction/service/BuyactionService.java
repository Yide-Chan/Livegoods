package com.livegoods.buyaction.service;

import com.livegoods.commons.pojo.Result;


public interface BuyactionService {
    Result<Object> buyAction(String id, String user);
}
