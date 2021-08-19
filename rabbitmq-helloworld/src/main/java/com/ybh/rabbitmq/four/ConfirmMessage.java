package com.ybh.rabbitmq.four;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.ybh.rabbitmq.utils.RabbitMqUtils;

import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * 发布确认模式：使用的时间，比较哪种确认方式是最好的
 * 1、单个确认
 * 2、批量确认
 * 3、异步批量确认
 */
public class ConfirmMessage {
    //批量发消息的个数
    public static final int MESSAGE_COUNT = 1000;

    public static void main(String[] args) throws Exception {
        //1、单个发布确认
        ConfirmMessage.publishMessageIndividually(); //发布1000条单独确认消息，耗时557ms
        //2、批量发布确认
        //ConfirmMessage.publishMessageBatch(); //发布1000条批量发布确认消息，耗时47ms
        //3、异步发布确认
        //ConfirmMessage.publishMessageAsync(); //发布1000条批量发布确认消息，耗时32ms
    }

    //1、单个发布确认
    public static void publishMessageIndividually() throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        //队列的声明
        //String queueName = UUID.randomUUID().toString();
        //channel.queueDeclare(queueName, true, false, false, null);
        String queueName = channel.queueDeclare().getQueue();
        //开启发布确认
        channel.confirmSelect();
        //开始时间
        long begin = System.currentTimeMillis();
        //批量发消息：单个发布确认
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("", queueName, null, message.getBytes());
            //单个消息进行发布确认
            boolean flag = channel.waitForConfirms();
            if (flag) {
                System.out.println("消息发送成功");
            }
        }
        //结束时间
        long end = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "条单独发布确认消息，耗时" + (end - begin) + "ms");
    }

    //2、批量发布确认
    public static void publishMessageBatch() throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        //队列的声明
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, true, false, false, null);
        //开启发布确认
        channel.confirmSelect();
        //开始时间
        long begin = System.currentTimeMillis();
        //批量确认消息的大小
        int batchSize = 100;
        //批量发消息：批量发布确认
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("", queueName, null, message.getBytes());
            //判断达到100条消息的时候，批量确认一次
            if (i%batchSize == 0) {
                //发布确认
                channel.waitForConfirms();
            }
        }
        //结束时间
        long end = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "条批量发布确认消息，耗时" + (end - begin) + "ms");
    }

    //3、异步发布确认
    public static void publishMessageAsync() throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        //队列的声明
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, true, false, false, null);
        //开启发布确认
        channel.confirmSelect();
        /**
         * 线程安全有序的跳表，适用于高并发的情况下
         * 1、轻松的将序号与消息进行关联（序号为key、内容为value）
         * 2、可以轻松的批量删除条目，只要给到序号
         * 3、支持高并发（多线程）
         */
        ConcurrentSkipListMap<Long, String> outStandingConfirms = new ConcurrentSkipListMap<>();
        //开始时间
        long begin = System.currentTimeMillis();
        //消息确认成功回调函数
        ConfirmCallback ackCallback = (deliveryTag, multiple) -> {
            if (multiple) {
                //2、删除掉已经确认的消息，剩下的就是未确认的消息
                ConcurrentNavigableMap<Long, String> confirmed = outStandingConfirms.headMap(deliveryTag);
                confirmed.clear();
            }else {
                outStandingConfirms.remove(deliveryTag);
            }
            System.out.println("确认的消息:" + deliveryTag);
        };
        //消息确认失败回调函数
        /**
         * 参数一：消息的标识
         * 参数二：是否为批量确认
         */
        ConfirmCallback nackCallback = (deliveryTag, multiple) -> {
            //3、打印一下未确认的消息都有哪些
            String message = outStandingConfirms.get(deliveryTag);
            System.out.println("未确认消息的标记:" + deliveryTag + " ====> 未确认的消息是:" + message);
        };
        //准备消息的监听器，监听哪些消息成功了，哪些消息失败了
        /**
         * 参数一：监听哪些消息成功了
         * 参数二：监听哪些消息失败了
         */
        channel.addConfirmListener(ackCallback, nackCallback); //异步
        //批量发消息：异步发布确认
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            //1、此处记录下所有要发送的消息，消息的总和
            outStandingConfirms.put(channel.getNextPublishSeqNo(), message);
            channel.basicPublish("", queueName, null, message.getBytes());
        }
        //结束时间
        long end = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "条批量发布确认消息，耗时" + (end - begin) + "ms");
    }
}
