package com.livegoods.buyaction.contorller;

import com.livegoods.buyaction.service.BuyactionService;
import com.livegoods.commons.pojo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class BuyactionContorller {
    @Autowired
    private BuyactionService buyactionService;
    public Result<Object> buyAction(String id, String user){
        return buyactionService.buyAction(id, user);
    }
}
