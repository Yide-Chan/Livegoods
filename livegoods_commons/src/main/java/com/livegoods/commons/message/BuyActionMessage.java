package com.livegoods.commons.message;

import lombok.Data;

import java.io.Serializable;

@Data
public class BuyActionMessage implements Serializable {
    private String houseId;

}
