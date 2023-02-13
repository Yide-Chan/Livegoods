package com.livegoods.message.consumer;

import com.codingapi.txlcn.tc.config.EnableDistributedTransaction;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@EnableDistributedTransaction
public class LivegoodsOrderMessageConsumerApp {
    public static void main(String[] args) {
        SpringApplication.run(LivegoodsOrderMessageConsumerApp.class,args);
    }
}
