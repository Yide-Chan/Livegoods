package com.livegoods.recommendation.dao.impl;

import com.livegoods.pojo.Items;
import com.livegoods.recommendation.dao.RecommendationDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
//@Repository注解修饰哪个类，则表明这个类具有对对象进行CRUD（增删改查）的功能
@Repository
public class RecommendationDaoImpl implements RecommendationDao {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Override
    public List<Items> findItems4Recommendation(Query query) {
        return mongoTemplate.find(query,Items.class);
    }
}
