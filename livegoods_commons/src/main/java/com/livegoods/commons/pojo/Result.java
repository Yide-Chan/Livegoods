package com.livegoods.commons.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 项目中返回结果类型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> implements Serializable {
    private Integer status;
    private List<T> results;
    private boolean hasMore;
    private long time;
    private String msg;
    public Result(Integer status,List results){
        this.status = status;
        this.results = results;
    }
    //推导属性（相当于起个别名）
    public List<T> getData(){
        return results;
    }
    public void setData(List<T> data){
        this.results = data;
    }
}
