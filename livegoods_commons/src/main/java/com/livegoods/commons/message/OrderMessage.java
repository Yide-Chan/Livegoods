package com.livegoods.commons.message;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderMessage implements Serializable {
    private String houseId;
    private String user;
}
