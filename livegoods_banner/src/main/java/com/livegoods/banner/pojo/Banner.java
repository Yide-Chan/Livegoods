package com.livegoods.banner.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Banner implements Serializable {
    private String id;
    private String path;
    private Date beginTime;
    private Date endTime;
}
