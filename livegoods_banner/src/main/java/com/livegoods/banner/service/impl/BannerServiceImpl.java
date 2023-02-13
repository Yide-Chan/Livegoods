package com.livegoods.banner.service.impl;

import com.livegoods.banner.dao.BannerDao;
import com.livegoods.banner.pojo.Banner;
import com.livegoods.banner.service.BannerService;
import com.livegoods.commons.pojo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 调用dao远程访问MongoDB,查询数据
 * 返回结果，做随机排序，抽取其中五条，不足的返回全部。
 */
@Service
public class BannerServiceImpl implements BannerService {
    //通过Value读取配置文件application.yml 的属性值
    @Value("${livegoods.banner.nginx.server}")
    private String bannerNginxServer;
    @Autowired
    private BannerDao bannerDao;

    @Override
    public Result<String> getBanners() {
        List<Banner> banners = bannerDao.getBanners();
        //随机排序
        Collections.shuffle(banners);
        //定义返回结果集合
        List<String> results = null;
        //判断banner集合长度，并初始化集合
        if (banners.size() > 5) {
            results = new ArrayList<>(5);
        } else {
            results = new ArrayList<>(banners.size());
        }
        //循环处理，把banner中的图片地址获取，并保存在结果集中
        int end = banners.size() > 5 ? 5 : banners.size();
        for (int i = 0; i < end; i++) {
            Banner banner = banners.get(i);
            results.add(bannerNginxServer + banner.getPath());
        }
        return new Result<>(200, results);
    }
}
