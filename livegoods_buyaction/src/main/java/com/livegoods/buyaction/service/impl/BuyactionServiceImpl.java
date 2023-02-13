package com.livegoods.buyaction.service.impl;

import com.livegoods.MessagePublisher;
import com.livegoods.buyaction.service.BuyactionService;
import com.livegoods.commons.message.BuyActionMessage;
import com.livegoods.commons.message.OrderMessage;
import com.livegoods.commons.pojo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class BuyactionServiceImpl implements BuyactionService {
    @Autowired
    private MessagePublisher messagePublisher;
    @Value("${livegoods.amqp.buyaction.exchange}")
    private String exchange;
    @Value("${livegoods.amqp.buyaction.rotingKey}")
    private String routingKey;
    @Value("${livegoods.amqp.order.exchange}")
    private String orderExchange;
    @Value("${livegoods.amqp.order.rotingKey}")
    private String orderRoutingKey;

    @Override
    public Result<Object> buyAction(String id, String user) {
        BuyActionMessage message = new BuyActionMessage();
        message.setHouseId(id);
        boolean buyResult = (boolean) messagePublisher.sendMessageSync(exchange, routingKey, message);

        Result<Object> result = new Result<>();
        if(buyResult){//秒杀成功
            //发送异步消息，创建订单，更新库存
            OrderMessage orderMessage = new OrderMessage();
            orderMessage.setHouseId(id);
            orderMessage.setUser(user);
            messagePublisher.sendMessageAsync(orderExchange, orderRoutingKey, orderMessage);

            result.setMsg("秒杀成功");
            result.setStatus(200);
        }else {//秒杀失败
            result.setMsg("秒杀失败");
            result.setStatus(500);
        }

        return result;
    }
}
