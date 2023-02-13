package com.livegoods.consumer.buyaction.listener;

import com.livegoods.commons.message.BuyActionMessage;
import com.livegoods.pojo.Houses;
import com.sun.tracing.dtrace.Attributes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class BuyActionMessagesConsumer {
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
    @Value("${livegoods.lock.buyaction}")
    private String lockKey;
    @Value("${livegoods.house.redisPrefix}")
    private String housePrefix;


    // 自旋超时阈值
    private final long times = 1000;

    private final String lockValue = "1";
    /**
     * 处理秒杀消息
     *  1、 访问redis，获取分布式锁。锁设定一个有效期。
     *  2、 判断锁是否获取成功。
     *  3、 获取失败，自旋等待，获取锁后，进入后续流程。
     *  4、 获取成功，访问redis，查询房屋数据
     *  5、 修改房屋数据库存
     *  6、 更新redis中的房屋缓存
     *  7、 释放分布式锁标记
     *  8、 返回秒杀结果
     * @param message
     * @return
     */
    @RabbitListener(bindings = {@QueueBinding(
            value = @Queue(name = "${livegoods.amqp.buyaction.queueName}", autoDelete = "false"),
            exchange = @Exchange(name = "${livegoods.amqp.buyaction.exchange}", type = "${livegoods.amqp.buyaction.exchangeType}"),
            key = "${livegoods.amqp.buyaction.routingKey}"
    )})
    public boolean onMessage(BuyActionMessage message){
        long localTimes = 0;
        while (localTimes < times) {
            if (redisTemplate.opsForValue().setIfAbsent(lockKey, lockValue, 2, TimeUnit.SECONDS)) {//分布式锁
                try {
                    String houseKey = housePrefix + "(" + message.getHouseId() + ")";
                    redisTemplate.opsForValue().get(houseKey);
                    Houses houses = (Houses) redisTemplate.opsForValue().get(houseKey);

                    if (houses.getNums() > 0) {
                        houses.setNums(houses.getNums() - 1);
                        // 更新商品数据到redis，保证后续其他逻辑可以得到秒杀后的结果
                        redisTemplate.opsForValue().set(houseKey, houses);
                        // 秒杀成功
                        return true;
                    } else {
                        return false;
                    }
                }finally {
                    // 一定删除锁标记
                    redisTemplate.delete(lockKey);
                }
            }
            // 获取锁失败
            try {
                localTimes += 100L;
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
