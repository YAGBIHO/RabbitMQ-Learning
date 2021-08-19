package com.ybh.rabbitmq.springboot.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置类：发布确认高级内容
 */
@Configuration
public class ConfirmConfig {
    //交换机名称
    public static final String CONFIRM_EXCHANGE_NAME = "confirm_exchange";
    //队列名称
    public static final String CONFIRM_QUEUE_NAME = "confirm_queue";
    //routingKey
    public static final String CONFIRM_ROUTING_KEY = "key1";
    //备份交换机名称
    public static final String BACKUP_EXCHANGE_NAME = "backup_exchange";
    //备份队列名称
    public static final String BACKUP_QUEUE_NAME = "backup_queue";
    //报警队列名称
    public static final String WARNING_QUEUE_NAME = "warning_queue";

    //声明交换机
    @Bean("confirmExchange")
    public DirectExchange confirmExchange() {
        return ExchangeBuilder.directExchange(CONFIRM_EXCHANGE_NAME)
                .durable(true)
                //设置该交换机的备份交换机
                .withArgument("alternate-exchange", BACKUP_EXCHANGE_NAME)
                .build();
    }

    //声明队列
    @Bean("confirmQueue")
    public Queue confirmQueue() {
        return QueueBuilder.durable(CONFIRM_QUEUE_NAME).build();
    }

    //绑定
    @Bean("queueBindingExchange")
    public Binding queueBindingExchange(@Qualifier("confirmQueue") Queue confirmQueue, @Qualifier("confirmExchange") DirectExchange confirmExchange) {
        return BindingBuilder.bind(confirmQueue).to(confirmExchange).with(CONFIRM_ROUTING_KEY);
    }

    //声明备份交换机
    @Bean("backupExchange")
    public FanoutExchange backupExchange() {
        return new FanoutExchange(BACKUP_EXCHANGE_NAME);
    }

    //声明备份队列
    @Bean("backQueue")
    public Queue backQueue() {
        return QueueBuilder.durable(BACKUP_QUEUE_NAME).build();
    }

    //声明报警队列
    @Bean("warningQueue")
    public Queue warningQueue() {
        return QueueBuilder.durable(WARNING_QUEUE_NAME).build();
    }

    //绑定报警队列与备份交换机
    @Bean
    public Binding warningQueueBindingBackupExchange(@Qualifier("warningQueue") Queue queue,
                                  @Qualifier("backupExchange") FanoutExchange backupExchange) {
        return BindingBuilder.bind(queue).to(backupExchange);
    }

    //绑定备份队列与备份交换机
    @Bean
    public Binding backupQueueBindingBackupExchange(@Qualifier("backQueue") Queue queue,
                                 @Qualifier("backupExchange") FanoutExchange backupExchange) {
        return BindingBuilder.bind(queue).to(backupExchange);
    }
}
