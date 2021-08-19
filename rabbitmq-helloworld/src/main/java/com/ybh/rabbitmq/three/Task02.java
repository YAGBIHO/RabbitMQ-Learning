package com.ybh.rabbitmq.three;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import com.ybh.rabbitmq.utils.RabbitMqUtils;

import java.util.Scanner;

/**
 * 消息在手动应答时不丢失、放回队列重新消费
 */
public class Task02 {
    //队列名称
    public static final String TASK_QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();
        //开启发布确认
        channel.confirmSelect();
        //声明一个队列
        boolean durable = true; //让队列进行持久化
        channel.queueDeclare(TASK_QUEUE_NAME, durable, false, false, null);
        //从控制台当中输入消息
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            //channel.basicPublish("", TASK_QUEUE_NAME, null, message.getBytes("UTF-8"));
            //设置生产发送消息为持久化消息（保存到磁盘上）
            channel.basicPublish("", TASK_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes("UTF-8"));
            System.out.println("生产者发出消息：" + message);
        }
    }
}
