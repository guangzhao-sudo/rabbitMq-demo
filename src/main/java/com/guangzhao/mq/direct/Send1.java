package com.guangzhao.mq.direct;

import com.guangzhao.mq.util.RabbitMqUtils;
import com.rabbitmq.client.Channel;

import java.util.Scanner;

public class Send1 {

    public static final String EXCHANGE_NAME="direct_logs";

    public static void main(String[] args) throws Exception {
        final Channel channel = RabbitMqUtils.getChannel();

        //channel.exchangeDeclare(EXCHANGE_NAME,"fanout");

        Scanner scanner=new Scanner(System.in);
        while (scanner.hasNext()){
            String message=scanner.next();

            channel.basicPublish(EXCHANGE_NAME,"info",null,message.getBytes());

            System.out.println("生产者发送消息"+message);
        }






    }
}
