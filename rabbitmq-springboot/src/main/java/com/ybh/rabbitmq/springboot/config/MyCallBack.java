package com.ybh.rabbitmq.springboot.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 发布确认高级的回调接口
 */
@Slf4j
@Component
public class MyCallBack implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnsCallback {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 将重写的回调方法、回退方法注入进RabbitTemplate中
     */
    @PostConstruct
    public void init() {
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnsCallback(this);
    }

    /**
     * 交换机确认回调方法：
     * 1、发消息交换机接收到了会回调
     * 2、发消息交换机接收失败了会回调
     * 参数一：保存回调消息的ID及相关信息
     * 参数二：交换机是否收到消息
     * 参数三：失败的原因（成功为null）
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String id = correlationData != null ? correlationData.getId() : "";
        if (ack) {
            log.info("交换机已经收到了ID为：{}的消息", id);
        }else {
            log.info("交换机还未收到ID为：{}的消息，原因是：{}", id, cause);
        }
    }

    /**
     * 可以在当消息传递过程中不可到达目的地时将消息返回给消息生产者
     * 只有不可达目的地的时候，才进行回退
     */
    @Override
    public void returnedMessage(ReturnedMessage returnedMessage) {
        log.error("消息：{}，被交换机：{}退回，退回的原因：{}，路由Key：{}",
                new String(returnedMessage.getMessage().getBody()),
                returnedMessage.getExchange(),
                returnedMessage.getReplyText(),
                returnedMessage.getRoutingKey());
    }
}
