package com.livegoods.hotproduct.service;

import com.livegoods.commons.pojo.Result;
import com.livegoods.pojo.Items;
import org.springframework.stereotype.Service;

import javax.management.Query;

@Service
public interface HotProductService {
    Result<Items> getHotProducts(String city);
}
