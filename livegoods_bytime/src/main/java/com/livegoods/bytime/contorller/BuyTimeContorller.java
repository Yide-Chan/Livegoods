package com.livegoods.bytime.contorller;

import com.livegoods.bytime.service.BuyTimeService;
import com.livegoods.commons.pojo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class BuyTimeContorller {
    @Autowired
    private BuyTimeService buyTimeService;
    @GetMapping("/buytime")
    public Result<Object> getHouseBuyTime(String id){
        return buyTimeService.getHouseBuyTime(id);
    }

}
