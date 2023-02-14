package com.guangzhao.mq.consumertest;

import com.guangzhao.mq.util.RabbitMqUtils;
import com.rabbitmq.client.Channel;

import java.util.Scanner;

public class Producer1 {

    public static  final  String QUEUE_NAME="guangzhao";

    public static void main(String[] args) throws Exception {
        final Channel channel = RabbitMqUtils.getChannel();


        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        //从控制台当中接受消息
        Scanner scanner=new Scanner(System.in);
        while(scanner.hasNext()){
            String message=scanner.next();
            channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
            System.out.println("发送消息完成"+message);


        }




    }
}
