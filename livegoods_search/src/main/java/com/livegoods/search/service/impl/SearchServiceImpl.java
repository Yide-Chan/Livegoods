package com.livegoods.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.livegoods.commons.pojo.Result;
import com.livegoods.pojo.Houses;
import com.livegoods.search.dao.SearchDao;
import com.livegoods.search.feign.LivegoodsDetailsService;
import com.livegoods.search.pojo.House4ES;
import com.livegoods.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

@Service
public class SearchServiceImpl implements SearchService {
    @Autowired
    private SearchDao searchDao;

    @Autowired
    private LivegoodsDetailsService livegoodsDetailsService;

    @Value("${livegoods.search.defaultRows}")
    private int defaultRows;
    
    @Value("${livegoods.nginx.server}")
    private String nginxServer;

    @Value("${livegoods.img.middle}")
    private String imgMiddle;

    @Override
    public boolean init() {
        List<Houses> list = livegoodsDetailsService.getAll();
        searchDao.saveHouses(mongo2ES(list));
        return false;
    }

    @Override
    public Result<House4ES> search(String city, String content, int page) {
        Map<String, Object> resultMap = searchDao.search(city, content, page, defaultRows);

        //总页数
        int pages = (int) resultMap.get("pages");

        //搜索结果集合
        List<House4ES> list = (List<House4ES>) resultMap.get("contents");
        Result<House4ES> result = new Result<>();

        //图片地址拼接
        for(House4ES house4ES: list){
            house4ES.setImg(nginxServer + imgMiddle + house4ES.getImg());
        }


        result.setResults(list);
        //是否还有更多数据；总页数减去当前页数是否大于1页
        result.setHasMore((pages - page) > 1);
        result.setStatus(200);
        return result;
    }

    /**
     * 把mongoDB对应的实体类型Houses转换成ES对应的实体类型House4ES
     * @param list
     * @return
     */
    private List<House4ES> mongo2ES(List<Houses> list){
        List<House4ES> result = new ArrayList<>();
        //转换规则
        for(Houses houses : list){
            House4ES house4ES = new House4ES();
            house4ES.setId(houses.getId());
            house4ES.setCity(houses.getCity());
            house4ES.setPrice("<h3>"+houses.getPrice()+"</h3>");
            house4ES.setRentType(houses.getRentType());
            Map<String, String> info = houses.getInfo();
            String houseType = info.get("level")+" | "+info.get("type")+" - "+houses.getHouseType();
            house4ES.setHouseType(houseType);
            house4ES.setTitle(houses.getTitle());
            String[] imgs = houses.getImgs();
            house4ES.setImg((imgs != null && imgs.length != 0) ? imgs[0] : "");
            result.add(house4ES);
        }
        return result;
    }

}
