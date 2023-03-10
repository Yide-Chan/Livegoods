package com.livegoods.passport.dao.mongo.impl;

import com.livegoods.passport.dao.mongo.PassportDao4Mongo;
import com.livegoods.pojo.LoginLog;
import com.livegoods.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
public class PassportDao4MongoImpl implements PassportDao4Mongo {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void insertPassportLog(LoginLog loginLog) {
        mongoTemplate.save(loginLog);
    }

    @Override
    public void insertUser(User user) {
        mongoTemplate.save(user);
    }

    @Override
    public void updateUser(User user) {
        Query query = Query.query(
                Criteria.where("_id").is(user.getId())
        );
        Update update = Update.update("lastLoginTime", user.getLastLoginTime());
        mongoTemplate.updateMulti(query, update, User.class);
    }

    @Override
    public User findByPhone(String phone) {
        Query query = Query.query(
                Criteria.where("phone").is(phone)
        );

        return mongoTemplate.findOne(query, User.class);
    }
}
