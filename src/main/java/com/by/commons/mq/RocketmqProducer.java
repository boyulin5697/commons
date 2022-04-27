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
 *Mq standard producer
 * @author by.
 * @date 2022/4/28
 */
@Component
@Slf4j
public class RocketmqProducer {

    @Resource
    private RocketMQTemplate rocketMQTemplate;

    /**
     * 基础发送异步消息方法
     * @param topic
     * @param contentMap
     */
    public void sendAsync(String topic, Map<String,Object> contentMap){
        rocketMQTemplate.asyncSend(topic, contentMap, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                log.warn("发送异步消息并处理成功,消费为{} ",sendResult.getSendStatus());
            }

            @Override
            public void onException(Throwable throwable) {
                log.error("异步消息处理失败或超时",throwable);
            }
        },300000);
    }

    /**
     * 延时消息发送方法
     * @param topic
     * @param contentMap
     * @param delayLevel
     */
    public void sendDelayMessage(String topic, Map<String,Object>contentMap, int delayLevel){
        rocketMQTemplate.syncSend(topic, MessageBuilder.withPayload(contentMap).build(),2000,delayLevel);
        log.info("延时消息已发送");
    }

    /**
     * 同步消息发送方法
     * @param topic
     * @param contentMap
     */
    public void sendSyncMessage(String topic, Map<String,Object>contentMap){
        rocketMQTemplate.syncSend(topic,contentMap);
        log.info("已发送同步消息");
    }
}
