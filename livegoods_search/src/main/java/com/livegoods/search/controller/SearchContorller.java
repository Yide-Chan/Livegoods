package com.livegoods.search.controller;

import com.livegoods.commons.pojo.Result;
import com.livegoods.search.pojo.House4ES;
import com.livegoods.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SearchContorller {
    @Autowired
    private SearchService searchService;

    /**
     * 初始化es数据
     * 不是任何人都可以访问的。必须经过登录认证，通过后，才能访问。
     * 使用Spring-Scurity来保证
     * @return
     */
    @GetMapping("/init")
    @CrossOrigin
    public String init(){
        searchService.init();
        //简单的初始化，有时间最好加上判断初始化是否成功的逻辑
        return "初始化ES完成";
    }

    /**
     * 搜索房屋，每页固定查询5条数据
     * @param city  必选  String。搜索条件，城市
     * @param content 必选     String。搜索条件，查询title、rentType、houseType等字段
     * @param page  必选 number。从0开始的页数。
     * @return
     */
    @GetMapping("/search")
    public Result<House4ES> search(String city, String content, int page){
        return searchService.search(city, content, page);
    }
}
