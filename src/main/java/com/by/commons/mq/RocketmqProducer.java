package com.by.commons.mq;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 *RocketMQ standard producer
 * @author by.
 * @date 2022/4/28
 */
@Component
@Slf4j
public class RocketmqProducer {

    @Resource
    private RocketMQTemplate rocketMQTemplate;

    /**
     * Basic async sending message
     * @param topic
     * @param contentMap
     */
    public void asyncSend(String topic, Map<String,Object> contentMap){
        rocketMQTemplate.asyncSend(topic, contentMap, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                log.info("Successfully send async message and result is {} ",sendResult.getSendStatus());
            }

            @Override
            public void onException(Throwable throwable) {
                log.error("Async message send failed",throwable);
            }
        },300000);
    }

    /**
     * Sending delay message, about delay level please see the
     * @param topic
     * @param contentMap
     * @param delayLevel
     */
    public void sendDelayMessage(String topic, Map<String,Object>contentMap, int delayLevel){
        rocketMQTemplate.syncSend(topic, MessageBuilder.withPayload(contentMap).build(),2000,delayLevel);
        log.info("Successfully send delay message!");
    }

    /**
     * 同步消息发送方法
     * @param topic
     * @param contentMap
     * @return sendResult
     */
    public SendResult sendMessage(String topic, Map<String,Object>contentMap){
        return rocketMQTemplate.syncSend(topic,contentMap);
    }
}
