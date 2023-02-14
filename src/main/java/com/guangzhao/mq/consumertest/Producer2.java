package com.guangzhao.mq.consumertest;

import com.guangzhao.mq.util.RabbitMqUtils;
import com.rabbitmq.client.Channel;

import java.util.Scanner;

public class Producer2 {
    public  final  static  String QUEUE_NAME="ack_auto";

    public static void main(String[] args) throws Exception {
        final Channel channel = RabbitMqUtils.getChannel();

        channel.queueDeclare(QUEUE_NAME,false,false,false,null);


        Scanner scanner=new Scanner(System.in);
        while (scanner.hasNext()){
             String message = scanner.next();
             channel.basicPublish("",QUEUE_NAME,null,message.getBytes("UTF-8"));
            System.out.println("发送消息" +message);

        }

    }

}
