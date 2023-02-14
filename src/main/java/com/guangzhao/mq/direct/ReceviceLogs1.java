package com.guangzhao.mq.direct;

import com.guangzhao.mq.util.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

public class ReceviceLogs1 {
    public static final String EXCHANGE_NAME="direct_logs";



    public static void main(String[] args) throws Exception {
        final Channel channel = RabbitMqUtils.getChannel();

        //声明一个交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        //声明一个队列
        channel.queueDeclare("console",false,false,false,null);
        channel.queueBind("console",EXCHANGE_NAME,"info");
        /**
         * 声明一个临时队列、队列的名称是随机的
         * 当消费者断开与队列的连接时，队列就自动删除
         */

        /**
         * 绑定交换机与队列
         */

        System.out.println("等待接受消息，把接受到的消息打印到屏幕上....");
        DeliverCallback deliverCallback=(consumerTag, message)->{
            System.out.println("控制台打印接受到的消息："+new String(message.getBody()));
        };
        CancelCallback cancelCallback= consumerTag->{
            System.out.println("取消消息时");
        };



        channel.basicConsume("console",true,deliverCallback,cancelCallback);
    }
}


