package com.livegoods.banner.controller;

import com.livegoods.banner.service.BannerService;
import com.livegoods.commons.pojo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BannerController {
    @Autowired
    private BannerService bannerService;
    /**
     * 前后端分离项目所有的请求都有前端发起，都是跨域请求
     * 控制器必须可以处理跨域请求，使用注解@CrossOrigin 来解决跨域
     * 轮播有有效时间，或者数据量级固定，不会无限膨胀。（商业中）
     * 用于处理轮播图查询逻辑
     * 每次访问返回5个轮播图，不足着返回所有查询结果。
     * 顺序随机
     * @return 可以使用map作为返回结果类型，或者自定义类型作为返回结果。
     * {
     *     status: 200,
     *     results: [
     *      "http://iwenwiki.com/api/livable/homehot/img_chuwugui.png",
     *      "http://iwenwiki.com/api/livable/homehot/img_chuwugui.png"
     *              ]
     * }
     */
    @GetMapping("/banner")
    @CrossOrigin
    public Result<String> getBanners(){
        return bannerService.getBanners();
    }

}
