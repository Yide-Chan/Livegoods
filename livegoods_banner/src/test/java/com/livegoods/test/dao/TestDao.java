package com.livegoods.test.dao;

import com.livegoods.banner.LivegoodsBannerApp;
import com.livegoods.banner.dao.BannerDao;
import com.livegoods.banner.pojo.Banner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest(classes = {LivegoodsBannerApp.class})
@RunWith(SpringRunner.class)
public class TestDao {
    @Autowired
    private BannerDao bannerDao;

    @Test
    public void testSelect() {
        List<Banner> banners = bannerDao.getBanners();
        for (Banner b : banners) {
            System.out.println(b);
        }
    }
}
