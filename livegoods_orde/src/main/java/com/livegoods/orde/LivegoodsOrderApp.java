package com.livegoods.orde;

import com.codingapi.txlcn.tc.config.EnableDistributedTransaction;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDistributedTransaction //开启事务
public class LivegoodsOrderApp {
    public static void main(String[] args) {
        SpringApplication.run(LivegoodsOrderApp.class,args);
    }
}
