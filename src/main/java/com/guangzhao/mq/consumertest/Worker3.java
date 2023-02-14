package com.guangzhao.mq.consumertest;

import com.guangzhao.mq.util.RabbitMqUtils;
import com.guangzhao.mq.util.SleepUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

public class Worker3 {
    public final static  String QUEUE_NAME="ack_auto";

    public static void main(String[] args) throws Exception {


        final Channel channel = RabbitMqUtils.getChannel();

        System.out.println("等待消息C4:");
        DeliverCallback deliverCallback =( consumerTag, message)->{
            SleepUtils.sleep(30);
            System.out.println("接受消息" +message);
            /**
             * 1.消息标记的Tag
             *2.是否批量应答
             */
        channel.basicAck(message.getEnvelope().getDeliveryTag(), false);







        };






        channel.basicConsume(QUEUE_NAME,false,deliverCallback,(consumerTag->{
            System.out.println("取消消息的回调逻辑");
        }));


    }


}
