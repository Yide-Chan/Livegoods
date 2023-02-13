package com.livegoods.bytime.service;

import com.livegoods.commons.pojo.Result;

public interface BuyTimeService {
    Result<Object> getHouseBuyTime(String id);
}
