package com.guangzhao.mq.deadqueue;

import com.guangzhao.mq.util.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.util.HashMap;
import java.util.Map;

public class Consumer1 {
    //正常交换机的名称
    public static final  String NORMAL_EXCHANGE="normal_exchange";
    //死信交换机的名称
    public static final  String DEAD_EXCHANGE="dead_exchange";

    //正常队列的名称
    public static final  String NORMAL_QUEUE="normal_queue";

    //死信队列的名称
    public static final  String DEAD_QUEUE="dead_queue";



    public static void main(String[] args) throws Exception {
        final Channel channel = RabbitMqUtils.getChannel();
        //声明死信交换机和正常交换机为direct
        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);

        //声明正常队列
        channel.queueDeclare(NORMAL_QUEUE,false,false,false,null);
        //过期时间
        //正常队列设置死信交换机
        Map<String,Object> map=new HashMap<>();
        map.put("x-dead-letter-exchange",DEAD_EXCHANGE);
        map.put("x-dead-letter-routing-key","lisi");
        //设置正常队列的最大限制长度
        map.put("x-max-length",6);
        channel.queueDeclare(DEAD_QUEUE,false,false,false,null);
        channel.queueBind(NORMAL_QUEUE,NORMAL_EXCHANGE,"zhangsan");
        channel.queueBind(DEAD_QUEUE,DEAD_EXCHANGE,"lisi");

        //声明接受消息的回调DeliverCallback  是一个接口  其后面的那个是参数
        DeliverCallback deliverCallback=(consumerTag, message)->{
            System.out.println("接受消息C1"+new String(message.getBody()));
        };
        //取消消息时的回调
        CancelCallback cancelCallback= consumerTag->{
            System.out.println("消息消费被中断1");
        };
        channel.basicConsume(NORMAL_QUEUE,true,deliverCallback,cancelCallback);

    }

}
