package com.livegoods.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class User implements Serializable {
    private String id;
    private String phone;
    private Date registerTime;
    private Date lastLoginTime;
}
