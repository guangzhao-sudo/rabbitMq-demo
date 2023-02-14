package com.guangzhao.mq.deadqueue;

import com.guangzhao.mq.util.RabbitMqUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;

public class Producer {
    //正常交换机的名称
    public static final  String NORMAL_EXCHANGE="normal_exchange";

    public static void main(String[] args) throws Exception {
        final Channel channel = RabbitMqUtils.getChannel();
       //死信消息  设置TTL时间  time to live
        AMQP.BasicProperties properties=new AMQP.BasicProperties()
                .builder().expiration("10000").build();

        for (int i = 0; i < 11; i++) {
            String message="info" +i;
            channel.basicPublish(NORMAL_EXCHANGE,"zhangsan",null,message.getBytes());



        }


    }

}
