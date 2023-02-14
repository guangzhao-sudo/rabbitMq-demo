package com.guangzhao.mq.confirm;

import com.guangzhao.mq.util.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;

import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

public class ConfirmMessage {

    public  static  final  int MESSAGE_COUNT=1000;

    /**
     * 1.单个确认
     * 2.批量确认
     * 3.异步批量确认
     */
    public static void main(String[] args) throws Exception {
        ConfirmMessage.publishMessageInividually();

    }


    //单个确认
    public static void publishMessageInividually() throws  Exception{
        final Channel channel = RabbitMqUtils.getChannel();
        //队列的声明
        String queueName= UUID.randomUUID().toString();
        channel.queueDeclare(queueName,false,false,false,null);
        //开启发布确认
        channel.confirmSelect();
        //开始时间
        final long begin = System.currentTimeMillis();

        //批量发消息
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message=i+"";
            channel.basicPublish("",queueName,null,message.getBytes());
       // 单个消息就马上进行发布确认
            final boolean b = channel.waitForConfirms();
            if(b){
                System.out.println("消息发送成功");

                //结束时间
                final long end = System.currentTimeMillis();
                System.out.println("发布"+MESSAGE_COUNT+"个单独确认消息，耗时"+(end-begin)+"ms");
        }

        }

    }


    public static void  publishMessageBatch() throws Exception{
        final Channel channel = RabbitMqUtils.getChannel();
        //队列的声明
        String queueName= UUID.randomUUID().toString();
        channel.queueDeclare(queueName,false,false,false,null);
        //开启发布确认
        channel.confirmSelect();
        //开始时间
        final long begin = System.currentTimeMillis();
        int batchSize=100;

        //批量发送消息 批量发布确认
        for (int i = 0; i <MESSAGE_COUNT ; i++) {
            String message=i+"";
            channel.basicPublish("",queueName,null,message.getBytes());

            //判断达到100条消息的时候，批量确认一次
            if(batchSize==100){
                //发布确认
                channel.waitForConfirms();
            }
        }
        final long end = System.currentTimeMillis();
        System.out.println("发布"+MESSAGE_COUNT+"个单独确认消息，耗时"+(end-begin)+"ms");

    }

    public static  void  publishMessageAsync () throws Exception {

        final Channel channel = RabbitMqUtils.getChannel();
        //队列的声明
        String queueName= UUID.randomUUID().toString();
        channel.queueDeclare(queueName,false,false,false,null);
        //开启发布确认
        channel.confirmSelect();
        /**
         * 线程安全有序的一个哈希表，使用于高并发的情况下
         * 1.轻松的将序号与消息进行关联
         * 2.轻松批量删除条目 只要给到序号
         * 3，支持高并发（多线程）
         */
        ConcurrentSkipListMap<Long,String>  outstandingConfirms=new ConcurrentSkipListMap<>();




        //准备消息的监听器 监听哪些消息成功了，哪些消息失败了
        //消息确认成功 回调函数
        ConfirmCallback ackCallback=(deliverTag,multiple)-> {
            if (multiple) {
                //2.删除已经确认的消息，剩下的就是未确认的消息
                final ConcurrentNavigableMap<Long, String> longStringConcurrentNavigableMap = outstandingConfirms.headMap(deliverTag);
                longStringConcurrentNavigableMap.clear();


            }else{
                outstandingConfirms.remove(deliverTag);
            }
            System.out.println("确认消息:"+deliverTag);
        };


        /**
         * 1.消息的标记
         * 2.是否为批量确认
         */
        //消息确认失败 回调函数
        ConfirmCallback nackCallback=(deliverTag,multiple)->{
            System.out.println("未确认消息:"+deliverTag);
        };

        channel.addConfirmListener(ackCallback,nackCallback);
        //开始时间
        final long begin = System.currentTimeMillis();
        //批量发送消息
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message=i+"";
            channel.basicPublish("",queueName,null,message.getBytes());
            //1.此处记录下所有要发送的消息， 消息的总和
            outstandingConfirms.put(channel.getNextPublishSeqNo(),message);

        }
        final long end = System.currentTimeMillis();
        System.out.println("发布"+MESSAGE_COUNT+"个单独确认消息，耗时"+(end-begin)+"ms");


    }

}
