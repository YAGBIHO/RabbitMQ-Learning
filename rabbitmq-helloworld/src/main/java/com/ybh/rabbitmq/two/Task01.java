package com.ybh.rabbitmq.two;

import com.rabbitmq.client.Channel;
import com.ybh.rabbitmq.utils.RabbitMqUtils;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * 生产者：发送大量的消息
 */
public class Task01 {
    //队列名称
    public static final String QUEUE_NAME = "hello";

    //发送大量的消息
    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        /**
         * 生成一个队列
         * 参数一：队列名称
         * 参数二：队列里面的消息是否持久化（磁盘），默认情况消息存储在内存中
         * 参数三：该队列是否只提供一个消费者进行消费，是否进行消息共享，true可以多个消费者消费；false只能一个消费者消费
         * 参数四：是否自动删除，最后一个消费者断开连接以后，该队列是否自动删除，true自动删除；false不自动删除
         */
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        //从控制台当中输入消息
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            /**
             * 发送一个消息
             * 参数一：发送到哪个交换机
             * 参数二：路由的Key是哪个，本次的队列的名称
             * 参数三：其它参数信息
             * 参数四：发送的消息的消息体
             */
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println("发送消息完成" + message);
        }
    }
}
