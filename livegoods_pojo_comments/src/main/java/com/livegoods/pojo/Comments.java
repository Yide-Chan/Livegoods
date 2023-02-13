package com.livegoods.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comments implements Serializable {
    private String id;
    private String houseId;
    private String username;
    private String comment;
    private int star;
}
