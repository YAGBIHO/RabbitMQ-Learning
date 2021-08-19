package com.ybh.rabbitmq.two;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.ybh.rabbitmq.utils.RabbitMqUtils;

/**
 * 工作线程01（相当于之前说的消费者）
 */
public class Worker01 {
    //队列名称
    public static final String QUEUE_NAME = "hello";

    //接收消息
    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();

        //消息成功接收的回调
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("接收到的消息：" + new String(message.getBody()));
        };
        //取消消费消息的回调
        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println(consumerTag + "消费者取消消费接口回调执行！");
        };

        /**
         * 消费者：消费消息
         * 参数一：消费哪个队列
         * 参数二：消费成功之后是否要自动应答，true代表自动应答；false代表手动应答
         * 参数三：消费者成功消费的回调
         * 参数四：消费者取消消费的回调
         */
        System.out.println("工作线程02正在等待......");
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);
    }
}
