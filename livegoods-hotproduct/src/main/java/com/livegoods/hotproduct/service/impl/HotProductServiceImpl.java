package com.livegoods.hotproduct.service.impl;

import com.livegoods.commons.pojo.Result;
import com.livegoods.hotproduct.dao.HotProductDao;
import com.livegoods.hotproduct.service.HotProductService;
import com.livegoods.pojo.Items;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HotProductServiceImpl implements HotProductService {
    @Autowired
    private HotProductDao hotProductDao;
    @Value("${livegoods.nginx.server}")
    private String nginxServer;

    @Override
    public Result<Items> getHotProducts(String city){
        Query query = Query.query(
                //条件只有”city“=city
                Criteria.where("city").is(city)
        );
        query.with(
                PageRequest.of(0, 4, Sort.by(Sort.Direction.DESC, "sales"))
        );
        List<Items> result = hotProductDao.findItems4HotProducts(query);

        if (result.size() < 4) {
            query = Query.query(
                    Criteria.where("city").ne(city)
            );
            query.with(
                    PageRequest.of(0, 4 - result.size(),
                            Sort.by(Sort.Direction.DESC, "sales"))
            );
            List<Items> others = hotProductDao.findItems4HotProducts(query);
            result.addAll(others);
        }
        // 为结果集合中的每个商品图片地址增加前缀
        for (Items i : result) {
            i.setImg(nginxServer + i.getImg());
        }
        return new Result<>(200, result);
    }
}
