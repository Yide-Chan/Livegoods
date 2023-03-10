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
     * ?????? ??????????????????
     * ????????????????????????
     *
     * @param city
     * @param content
     * @param page
     * @param rows    ?????????????????????
     * @return
     */
    @Override
    public Map<String, Object> search(String city, String content, int page, int rows) {
        BoolQueryBuilder builder = QueryBuilders.boolQuery();
        // ??????????????????,?????????????????????????????????????????????
        builder.must(QueryBuilders.matchPhraseQuery("city", city));
        // ?????????????????????????????????
        builder.should(QueryBuilders.matchQuery("title", content));
        builder.should(QueryBuilders.matchQuery("houseType", content));
        builder.should(QueryBuilders.matchQuery("rentType", content));

        // ????????????????????? ???????????????????????????????????????
        HighlightBuilder.Field titleField = new HighlightBuilder.Field("title");
        //???????????????????????????
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

        // ??????????????????????????????NativeSearchQuery
        NativeSearchQuery query =
                new NativeSearchQueryBuilder()
                        .withQuery(builder) // ????????????
                        .withHighlightFields(titleField, houseTypeField, rentTypeField)//??????
                        .withPageable(
                                PageRequest.of(page, rows)//??????
                        )
                        .build();
        //AggregatedPage<House4ES> resultPage = restTemplate.queryForPage(query, House4ES.class, new SearchResultMapper(){
        // ????????????????????????????????????????????????search??????
        SearchHits<House4ES> search = restTemplate.search(query, House4ES.class);
        // ???Search????????????????????????
        SearchPage<House4ES> searchPageFor = SearchHitSupport.searchPageFor(search, query.getPageable());

        // ?????????????????????????????????
        List<SearchHit<House4ES>> searchHits = search.getSearchHits();

        // ??????????????????????????????????????????
        List<House4ES> resultList = new ArrayList<>();
        // ?????????????????????????????????
        for (SearchHit<House4ES> searchHit : searchHits) {
            House4ES house4ES = new House4ES();
            house4ES.setId(searchHit.getContent().getId());
            house4ES.setPrice(searchHit.getContent().getPrice());
//            house4ES.setImg(searchHit.getContent().getImg());
            house4ES.setCity(searchHit.getContent().getCity());

            // ???????????????
            Map<String, List<String>> highLightFields = searchHit.getHighlightFields();
            // ???????????????????????????content???
            searchHit.getContent().setTitle(highLightFields.get("title") == null ? searchHit.getContent().getTitle() : highLightFields.get("title").get(0));
            searchHit.getContent().setHouseType(highLightFields.get("houseType") == null ? searchHit.getContent().getHouseType() : highLightFields.get("houseType").get(0));
            searchHit.getContent().setRentType(highLightFields.get("rentType") == null ? searchHit.getContent().getRentType() : highLightFields.get("rentType").get(0));
//            searchHit.getContent().setImg(nginxServer + imgMiddle + searchHit.getContent().getImg());

            house4ES.setTitle(searchHit.getContent().getTitle());
            house4ES.setImg(searchHit.getContent().getImg());
            house4ES.setHouseType(searchHit.getContent().getHouseType());
            house4ES.setRentType(searchHit.getContent().getRentType());

            //????????????????????????
//            House4ES house4ES = JSON.parseObject(JSON.toJSONString(searchHit), House4ES.class);

            // ??????????????????
//            resultList.add(searchHit.getContent());
            resultList.add(house4ES);
        }



        Map<String, Object> resultMap = new HashMap<>();
        // ????????????
        //long totalElements = searchPageFor.getTotalElements();
        // ?????????
        //int totalPages = searchPageFor.getTotalPages();
        // ????????????
        //int currentPage = searchPageFor.getPageable().getPageNumber();
        // ?????????????????????
        //int currentPageRows = searchPageFor.getPageable().getPageSize();

        // ????????????
        resultMap.put("pages", searchPageFor.getTotalPages());
        // ??????????????????????????????
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
