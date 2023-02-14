package com.guangzhao.mq.test;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

//生产者发消息
public class Producer {
    public static  final  String QUEUE_NAME="guangzhao";

    public static void main(String[] args) throws IOException, TimeoutException {

        ConnectionFactory factory =new ConnectionFactory();
        factory.setHost("192.168.174.131");
        factory.setUsername("guest");
        factory.setPassword("guest");

        //创建连接
        Connection connection=factory.newConnection();
        //获取信道
         Channel channel = connection.createChannel();
         //生成一个队列1.队列名称
        //2.队列里面得消息是否持久化
        //3.该队列是否只供一个消费者消费，是否进行消息共享，true可以多个消费者
        //4.是否自动删除，最后一个消费者端开连接以后该队一句是否自动删除，true自动删除，false不自动删除
        //5，其他参数
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
         //发消息
        String message="hello world1";//初次使用
        //发送一个消费
        /**
         * 1.发生到那个交换机
         * 2.路由得key值是哪个，本次是队列得名称
         * 3.其他参数信息
         * 4。发送消息得消息体
         *
         */
        channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
        System.out.println("发了");
    }


}
