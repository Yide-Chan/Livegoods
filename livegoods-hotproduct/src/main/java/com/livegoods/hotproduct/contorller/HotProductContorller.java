package com.livegoods.hotproduct.contorller;

import com.livegoods.commons.pojo.Result;
import com.livegoods.hotproduct.service.HotProductService;
import com.livegoods.pojo.Items;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class HotProductContorller {
    @Autowired
    private HotProductService hotProductService;
    @GetMapping("/hotProduct")
    public Result<Items> getHotProduct(String city){
        return hotProductService.getHotProducts(city);
    }

}
