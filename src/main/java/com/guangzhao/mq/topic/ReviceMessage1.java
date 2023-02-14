package com.guangzhao.mq.topic;

import com.guangzhao.mq.util.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

public class ReviceMessage1 {
    public static  final String EXCHANGE_NAME="topic_logs";

    public static void main(String[] args) throws Exception {
        final Channel channel = RabbitMqUtils.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        channel.queueDeclare("Q1",false,false,false,null);
        DeliverCallback deliverCallback=(consumerTag, message)->{
            System.out.println("控制台打印接受到的消息C1："+message.getEnvelope().getRoutingKey());
        };
        CancelCallback cancelCallback= consumerTag->{
            System.out.println("取消消息时");
        };


        channel.queueBind("Q1",EXCHANGE_NAME,"*.orange.*");
        System.out.println("等待接受消息....");
        channel.basicConsume("Q1",true,deliverCallback,cancelCallback);


    }

}
