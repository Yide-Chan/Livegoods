package com.livegoods.feedback.dao.impl;

import com.livegoods.feedback.dao.FeedbackDao;
import com.livegoods.pojo.Comments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;


@Repository
public class FeedbackDaoImpl implements FeedbackDao {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void saveFeedback(Comments comments) {
        mongoTemplate.save(comments);
    }

    @Override
    public void removeByComments(Comments comments) {
        Query query = Query.query(
                Criteria.where("username").is(comments.getUsername())
                .and("star").is(comments.getStar())
                .and("houseId").is(comments.getHouseId())
                .and("comment").is(comments.getComment())
        );
        mongoTemplate.remove(query,Comments.class);
    }
}
