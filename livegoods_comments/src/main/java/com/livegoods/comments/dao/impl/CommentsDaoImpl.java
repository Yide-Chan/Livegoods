package com.livegoods.comments.dao.impl;

import com.livegoods.comments.dao.CommentsDao;
import com.livegoods.pojo.Comments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class CommentsDaoImpl implements CommentsDao {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<Comments> findCommentsByHouseId(String id, int page, int rows) {
        // 搜索条件
        Query query = Query.query(
                Criteria.where("houseId").is(id)
        );
        // 分页
        query.with(
                PageRequest.of(page,rows, Sort.by(Sort.Direction.DESC,"_id"))
        );
        return mongoTemplate.find(query,Comments.class);
    }

    /**
     * 聚合查询
     * @param id
     * @return
     */
    @Override
    public long findCommentsRowsByHouseId(String id) {
        TypedAggregation<Comments> type = TypedAggregation.newAggregation(
                Comments.class,
                Aggregation.match(Criteria.where("houseId").is(id)),
                Aggregation.group()// 按什么分组，，这里不分组
                        .count() // 总计
                        .as("rows")  // 起个别名
        );
        AggregationResults<Map> results = mongoTemplate.aggregate(type, Map.class);
        List<Map> list = results.getMappedResults();
        if(list == null || list.size() == 0){
            return 0L;
        }
        return Long.parseLong(list.get(0).get("rows").toString());
    }
}
