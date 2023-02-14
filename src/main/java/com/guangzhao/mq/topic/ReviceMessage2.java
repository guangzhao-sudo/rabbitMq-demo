package com.guangzhao.mq.topic;

import com.guangzhao.mq.util.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

public class ReviceMessage2 {
    public static  final String EXCHANGE_NAME="topic_logs";

    public static void main(String[] args) throws Exception {
        final Channel channel = RabbitMqUtils.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        channel.queueDeclare("Q2",false,false,false,null);
        DeliverCallback deliverCallback=(consumerTag, message)->{
            System.out.println("控制台打印接受到的消息C2："+message.getEnvelope().getRoutingKey());
        };
        CancelCallback cancelCallback= consumerTag->{
            System.out.println("取消消息时");
        };


        channel.queueBind("Q2",EXCHANGE_NAME,"*.*.rabbit");
        channel.queueBind("Q2",EXCHANGE_NAME,"lazy.#");
        System.out.println("等待接受消息....");
        channel.basicConsume("Q2",true,deliverCallback,cancelCallback);


    }

}
