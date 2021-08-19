package com.ybh.rabbitmq.one;

import com.rabbitmq.client.*;

/**
 * 消费者：接收消息
 */
public class Consumer {
    //队列名称
    public static final String QUEUE_NAME = "hello";

    //接收消息
    public static void main(String[] args) throws Exception{
        //创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //工厂IP，连接RabbitMQ的队列
        factory.setHost("192.168.10.202");
        //用户名、密码
        factory.setUsername("YAGBIHO");
        factory.setPassword("217123");
        //创建连接
        Connection connection = factory.newConnection();
        //获取信道
        Channel channel = connection.createChannel();
//        channel.exchangeDeclare("fed_exchange", BuiltinExchangeType.DIRECT);
//        channel.queueDeclare("node2_queue", true, false, false, null);
//        channel.queueBind("node2_queue", "fed_exchange", "routeKey");

        //消息成功接收的回调
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println(new String(message.getBody()));
        };
        //取消消费消息的回调
        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println("消息消费被中断！");
        };

        /**
         * 消费者：消费消息
         * 参数一：消费哪个队列
         * 参数二：消费成功之后是否要自动应答，true代表自动应答；false代表手动应答
         * 参数三：消费者成功消费的回调
         * 参数四：消费者取消消费的回调
         */
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);
    }
}
