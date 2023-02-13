package com.livegoods.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Items implements Serializable {
    private String id;
    private String title;
    private String img;
    private String city;
    /**
     * 销售数量
     */
    private Long sales;
    /**
     * 是否推荐
     */
    private Boolean recommendation;
    /**
     * 推荐排序
     */
    private int recommendationSort;

    /**
     * link 连接地址
     */
    public String getLink() {
        return "/items/" + id;
    }

    /**
     * 空处理，防止Jackson反序列化抛异常
     */
    public void setLink(String link) {

    }
}
