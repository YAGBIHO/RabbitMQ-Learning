package com.ybh.rabbitmq.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * 连接工厂创建信道的工具类
 */
public class RabbitMqUtils {
    //得到一个连接的channel
    public static Channel getChannel() throws Exception {
        //创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //工厂IP，连接RabbitMQ的队列
        factory.setHost("192.168.10.100");
        //用户名、密码
        factory.setUsername("YAGBIHO");
        factory.setPassword("217123");
        //创建连接
        Connection connection = factory.newConnection();
        //获取信道
        Channel channel = connection.createChannel();
        return channel;
    }
}
