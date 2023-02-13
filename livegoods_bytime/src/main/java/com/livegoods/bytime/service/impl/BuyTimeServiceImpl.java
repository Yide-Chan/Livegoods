package com.livegoods.bytime.service.impl;

import com.livegoods.bytime.dao.BuyTimeDao;
import com.livegoods.bytime.service.BuyTimeService;
import com.livegoods.commons.pojo.Result;
import com.livegoods.pojo.Houses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BuyTimeServiceImpl implements BuyTimeService {
    @Autowired
    private BuyTimeDao buyTimeDao;

    @Override
    public Result<Object> getHouseBuyTime(String id) {
        Houses houses = buyTimeDao.findHouseById(id);
        if(houses == null){
            return new Result<>();
        }
        System.out.println(houses.getBuytime());
        long buytime = houses.getBuytime().getTime();
        Result<Object> result = new Result<>();
        result.setTime(buytime);
        result.setStatus(200);
        return result;
    }
}
