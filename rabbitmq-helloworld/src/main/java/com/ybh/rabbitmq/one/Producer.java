package com.ybh.rabbitmq.one;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 生产者：发消息
 */
public class Producer {
    //队列名称
    public static final String QUEUE_NAME = "hello";

    //发消息
    public static void main(String[] args) throws Exception{
        //创建一个连接工厂
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
        /**
         * 生成一个队列
         * 参数一：队列名称
         * 参数二：队列里面的消息是否持久化（磁盘），默认情况消息存储在内存中
         * 参数三：该队列是否只提供一个消费者进行消费，是否进行消息共享，true可以多个消费者消费；false只能一个消费者消费
         * 参数四：是否自动删除，最后一个消费者断开连接以后，该队列是否自动删除，true自动删除；false不自动删除
         */
        //设置优先级队列
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-max-priority", 10); //官方允许是0-255之间，此处允许优先级范围是0-10
        channel.queueDeclare(QUEUE_NAME, true, false, false, arguments);
        //发消息
        for (int i = 1; i < 11; i++) {
            String message = "info" + i;
            if (i == 5) {
                AMQP.BasicProperties properties = new AMQP.BasicProperties().builder().priority(5).build();
                channel.basicPublish("", QUEUE_NAME, properties, message.getBytes());
            }else {
                channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            }
        }
        String message = "Hello World";
        /**
         * 发送一个消息
         * 参数一：发送到哪个交换机
         * 参数二：路由的Key是哪个，本次的队列的名称
         * 参数三：其它参数信息
         * 参数四：发送的消息的消息体
         */
        //channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
        System.out.println("消息发送完毕！");
    }
}
