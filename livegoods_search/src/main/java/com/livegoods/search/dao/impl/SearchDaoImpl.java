package com.livegoods.search.dao.impl;

import com.alibaba.fastjson.JSON;
import com.livegoods.search.dao.SearchDao;
import com.livegoods.search.pojo.House4ES;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.*;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Repository;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class SearchDaoImpl implements SearchDao {
    @Autowired
    private ElasticsearchRestTemplate restTemplate;

    @Autowired
    private RestHighLevelClient restHighLevelClient;

//    @Value("${livegoods.nginx.server}")
//    private String nginxServer;
//
//    @Value("${livegoods.img.middle}")
//    private String imgMiddle;

    @Override
    public void saveHouses(List<House4ES> list) {
//        List<IndexQuery> queries = new ArrayList<>();
//        for(House4ES h : list){
//            queries.add(new IndexQueryBuilder().withObject(h).build());
//        }
//        restTemplate.bulkIndex(queries);
        restTemplate.save(list);
    }

    /**
     * 搜索 高亮处理结果
     * 模糊匹配搜索条件
     *
     * @param city
     * @param content
     * @param page
     * @param rows    展示多少行数据
     * @return
     */
    @Override
    public Map<String, Object> search(String city, String content, int page, int rows) {
        BoolQueryBuilder builder = QueryBuilders.boolQuery();
        // 城市等值判断,短语搜索，条件不分词。必要条件
        builder.must(QueryBuilders.matchPhraseQuery("city", city));
        // 标题模糊匹配，可选条件
        builder.should(QueryBuilders.matchQuery("title", content));
        builder.should(QueryBuilders.matchQuery("houseType", content));
        builder.should(QueryBuilders.matchQuery("rentType", content));

        // 高亮字段包括： 标题，房屋类型，租赁方式。
        HighlightBuilder.Field titleField = new HighlightBuilder.Field("title");
        //对关键字段进行设定
        titleField.preTags("<span style='color:red'>");
        titleField.postTags("</span>");
        titleField.fragmentSize(10);
        titleField.numOfFragments(1);

        HighlightBuilder.Field houseTypeField = new HighlightBuilder.Field("houseType");
        houseTypeField.preTags("<span style='color:red'>");
        houseTypeField.postTags("</span>");
        houseTypeField.fragmentSize(10);
        houseTypeField.numOfFragments(1);

        HighlightBuilder.Field rentTypeField = new HighlightBuilder.Field("rentType");
        rentTypeField.preTags("<span style='color:red'>");
        rentTypeField.postTags("</span>");
        rentTypeField.fragmentSize(10);
        rentTypeField.numOfFragments(1);

        // 搜索条件构造器构建：NativeSearchQuery
        NativeSearchQuery query =
                new NativeSearchQueryBuilder()
                        .withQuery(builder) // 搜索条件
                        .withHighlightFields(titleField, houseTypeField, rentTypeField)//高亮
                        .withPageable(
                                PageRequest.of(page, rows)//分页
                        )
                        .build();
        //AggregatedPage<House4ES> resultPage = restTemplate.queryForPage(query, House4ES.class, new SearchResultMapper(){
        // 执行搜索，获取封装响应数据结果的search集合
        SearchHits<House4ES> search = restTemplate.search(query, House4ES.class);
        // 对Search集合进行分页封装
        SearchPage<House4ES> searchPageFor = SearchHitSupport.searchPageFor(search, query.getPageable());

        // 得到查询结果返回的内容
        List<SearchHit<House4ES>> searchHits = search.getSearchHits();

        // 设置一个需要返回的实体类集合
        List<House4ES> resultList = new ArrayList<>();
        // 遍历返回的内容进行处理
        for (SearchHit<House4ES> searchHit : searchHits) {
            House4ES house4ES = new House4ES();
            house4ES.setId(searchHit.getContent().getId());
            house4ES.setPrice(searchHit.getContent().getPrice());
//            house4ES.setImg(searchHit.getContent().getImg());
            house4ES.setCity(searchHit.getContent().getCity());

            // 高亮的内容
            Map<String, List<String>> highLightFields = searchHit.getHighlightFields();
            // 将高亮的内容填充到content中
            searchHit.getContent().setTitle(highLightFields.get("title") == null ? searchHit.getContent().getTitle() : highLightFields.get("title").get(0));
            searchHit.getContent().setHouseType(highLightFields.get("houseType") == null ? searchHit.getContent().getHouseType() : highLightFields.get("houseType").get(0));
            searchHit.getContent().setRentType(highLightFields.get("rentType") == null ? searchHit.getContent().getRentType() : highLightFields.get("rentType").get(0));
//            searchHit.getContent().setImg(nginxServer + imgMiddle + searchHit.getContent().getImg());

            house4ES.setTitle(searchHit.getContent().getTitle());
            house4ES.setImg(searchHit.getContent().getImg());
            house4ES.setHouseType(searchHit.getContent().getHouseType());
            house4ES.setRentType(searchHit.getContent().getRentType());

            //将集合转为实体类
//            House4ES house4ES = JSON.parseObject(JSON.toJSONString(searchHit), House4ES.class);

            // 放到实体类中
//            resultList.add(searchHit.getContent());
            resultList.add(house4ES);
        }



        Map<String, Object> resultMap = new HashMap<>();
        // 总记录数
        //long totalElements = searchPageFor.getTotalElements();
        // 总页数
        //int totalPages = searchPageFor.getTotalPages();
        // 当前页数
        //int currentPage = searchPageFor.getPageable().getPageNumber();
        // 当前页总记录数
        //int currentPageRows = searchPageFor.getPageable().getPageSize();

        // 总计页数
        resultMap.put("pages", searchPageFor.getTotalPages());
        // 当前也显示的数据集合
//        resultMap.put("contents", searchPageFor.getContent().get(0).getContent());
        resultMap.put("contents", resultList);

//        SearchRequest request = new SearchRequest("livegoods_houses");
//        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
//        sourceBuilder.query(QueryBuilders.matchAllQuery())
//                .from(0)
//                .size(10);
//
//        request.source(sourceBuilder);
//        SearchResponse response = restTemplate.search(request, RequestOptions.DEFAULT);
//

        return resultMap;
    }
}
