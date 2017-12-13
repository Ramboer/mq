package com.intelligent.mqpredic.rocket;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by simon.liu on 2017/12/12.
 */
public class Consumer {
    private static final String CONSUMER_GROUP = "Broadcast_Consumer_GROUP";

    /**
     *
     * @param args args
     * @throws MQClientException MQ Client Exception
     */
    public static void main(String[] args) throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(CONSUMER_GROUP);
        consumer.setNamesrvAddr(Producer.NAMESRV_ADDR);
        consumer.setMessageModel(MessageModel.CLUSTERING);
        consumer.subscribe(Producer.TOPIC, "*");
        consumer.registerMessageListener(new MessageListenerConcurrently() {

            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                final String msg = msgs.stream().map(MessageExt::getBody)
                        .map(t -> new String(t, StandardCharsets.UTF_8))
                        .collect(Collectors.joining("   , "));
                System.out.println(msg);
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });

        consumer.start();
    }
}
