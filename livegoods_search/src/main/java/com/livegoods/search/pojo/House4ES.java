package com.livegoods.search.pojo;

import lombok.Data;
import org.elasticsearch.search.SearchHit;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;

@Data
//indexName 索引
@Document(indexName = "livegoods_houses")
public class House4ES implements Serializable {
    @Id
    private String id;
    private String title;
    private String houseType;
    private String rentType;
    private String img;
    private String city;
    private String price;
}
