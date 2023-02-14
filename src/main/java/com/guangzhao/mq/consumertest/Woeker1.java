package com.guangzhao.mq.consumertest;


import com.guangzhao.mq.util.RabbitMqUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

public class Woeker1 {
    public static  final String  QUEUE_NAME="guangzhao";

    public static void main(String[] args) throws Exception {

        //声明接受消息的回调DeliverCallback  是一个接口  其后面的那个是参数
        DeliverCallback deliverCallback=(consumerTag, message)->{
            System.out.println("接受消息"+new String(message.getBody()));

        };
        //取消消息时的回调
        CancelCallback cancelCallback= consumerTag->{
            System.out.println("消息消费被中断1");

        };


        System.out.println("C2等待接受消息");
        final Channel channel = RabbitMqUtils.getChannel();
        channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);


    }
}
