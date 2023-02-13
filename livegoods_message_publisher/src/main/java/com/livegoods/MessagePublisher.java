package com.livegoods;

import org.springframework.amqp.core.AmqpTemplate;

public class MessagePublisher {
    private AmqpTemplate amqpTemplate;

    /**
     * 发送同步消息
     * @param exchange
     * @param routingKey
     * @param messagePayload
     * @return
     */
    public Object sendMessageSync(String exchange, String routingKey, Object messagePayload){
        return amqpTemplate.convertSendAndReceive(exchange, routingKey, messagePayload);
    }

    /**
     * 发送异步消息
     * @param exchange
     * @param routingKey
     * @param messagePayload
     */
    public void sendMessageAsync(String exchange, String routingKey, Object messagePayload){
        amqpTemplate.convertAndSend(exchange, routingKey, messagePayload);
    }

    public AmqpTemplate getAmqpTemplate() {
        return amqpTemplate;
    }

    public void setAmqpTemplate(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }
}
