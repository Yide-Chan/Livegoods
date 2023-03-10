package com.livegoods.houses.dao;

import com.livegoods.commons.pojo.Result;
import com.livegoods.pojo.Houses;

import java.util.List;

public interface HousesDao {
    List<Houses> findAll();
    // 根据主键查询房屋。
    Houses findById(String id);
    //更新房屋库存
    void updateHousesNum(Houses houses);
}
