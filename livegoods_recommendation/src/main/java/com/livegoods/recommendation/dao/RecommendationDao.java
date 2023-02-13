package com.livegoods.recommendation.dao;

import com.livegoods.pojo.Items;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public interface RecommendationDao {

    /**
     * 根据条件查询商品数据
     * 因为查询出来的有城市等于4和城市不等于4的情况，所以使用Query
     * @param query 具体的查询条件，包括条件和排序规则。
     * @return
     */
    List<Items> findItems4Recommendation(Query query);
}
