package com.livegoods.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class LoginLog implements Serializable {
    private String id;
    private String username;
    private Date currentTime;
    private String type;
    private String message;
    private String status;

    public static final String VALIDATE_CODE = "验证码登录";
    public static final String PASSWORD = "密码登录";
    public static final String SUCCESS = "登录成功";
    public static final String ERROR = "登录失败";
}
