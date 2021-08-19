package com.ybh.rabbitmq.utils;

/**
 * 睡眠工具类
 * 让线程等待X秒再执行
 */
public class SleepUtils {
    public static void sleep(int second) {
        try {
            Thread.sleep(1000 * second);
        }catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
