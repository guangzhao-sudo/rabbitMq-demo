package com.guangzhao.mq.test;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer {

    public static  final  String QUEUE_NAME="guangzhao";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory=new ConnectionFactory();
        factory.setHost("192.168.174.131");
        factory.setUsername("guest");
        factory.setPassword("guest");

        Connection connection=factory.newConnection();
         Channel channel = connection.createChannel();
         //声明接受消息的回调DeliverCallback  是一个接口  其后面的那个是参数
        DeliverCallback deliverCallback=(consumerTag,message)->{
            System.out.println(new String(message.getBody()));

        };
        //取消消息时的回调
        CancelCallback cancelCallback=consumerTag->{
            System.out.println("消息消费被中断");

        };

        /**消费者消息
         * 1.消费哪个队列
         * 2.消费成功之后是否要自动应答，true代表得自动应答，false代表手动应答
         * 3.消费者未成功消费的回调
         * 4.消费者取消消费的回调
         */
        System.out.println("C1等待接受消息");
        channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);
    }
}
