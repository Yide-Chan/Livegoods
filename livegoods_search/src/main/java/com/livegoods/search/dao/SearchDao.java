package com.livegoods.search.dao;

import com.livegoods.search.pojo.House4ES;

import java.util.List;
import java.util.Map;

public interface SearchDao {
    //批量保存房屋到es
    void saveHouses(List<House4ES> list);

    /**
     * 搜索，高亮！！！
     * 仅供微服务内部使用，所以没有解耦合，
     * @param city
     * @param content
     * @param page
     * @param rows 展示多少行数据
     * @return
     *      {
     *          "pages":总计页数，数学类型
     *          "contents":[当前页要显示的房屋集合]
     *      }
     */
    Map<String, Object> search(String city, String content, int page, int rows);
}
