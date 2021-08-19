package com.ybh.rabbitmq.three;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.ybh.rabbitmq.utils.RabbitMqUtils;
import com.ybh.rabbitmq.utils.SleepUtils;

/**
 * 消息在手动应答时不丢失、放回队列重新消费
 * 消费者02
 */
public class Worker03 {
    //队列名称
    public static final String TASK_QUEUE_NAME = "ack_queue";

    //接收消息
    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();
        System.out.println("C2等待接收消息处理时间较长！");

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            //沉睡1秒
            SleepUtils.sleep(30);
            System.out.println("接收到的消息：" + new String(message.getBody(), "UTF-8"));
            //手动应答
            /**
             * 参数一：消息的标识 tag
             * 参数二：是否批量应答，true批量；false不批量应答信道中的消息
             */
            channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
        };
        //设置不公平分发
        //int prefetchCount = 1;
        //预取值是2
        int prefetchCount = 5;
        channel.basicQos(prefetchCount);
        //采用手动应答
        boolean autoAck = false;
        channel.basicConsume(TASK_QUEUE_NAME, autoAck, deliverCallback, (consumerTag -> {
            System.out.println(consumerTag + "消费者取消消费接口回调！");
        }));
    }
}
