package com.ybh.rabbitmq.five;

import com.rabbitmq.client.Channel;
import com.ybh.rabbitmq.utils.RabbitMqUtils;

import java.util.Scanner;

/**
 * 发消息给交换机
 */
public class EmitLog {
    //交换机名字
    public static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        /**
         * 声明一个交换机
         * 参数一：交换机的名称
         * 参数二：交换机的类型
         */
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes("UTF-8"));
            System.out.println("生产者发送的消息：" + message);
        }
    }
}
