package com.by.commons.mq;

import com.by.commons.logs.RocketMQLogDao;
import com.by.commons.tools.UuidTool;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

/**
 * RocketMQ standard producer
 * @author by.
 * @date 2022/4/28
 */
@Component
@Slf4j
public class RocketmqProducer {

    @Resource
    private RocketMQTemplate rocketMQTemplate;


    @Autowired
    private RocketMQLogDao logDao;

    /**
     * Basic async sending message
     * @param topic
     * @param contentMap
     */
    public void asyncSend(String topic, Map<String,Object> contentMap){
        Assert.hasText(topic,"topic cannot be null");
        Assert.notNull(contentMap,"message map cannot be empty!");
        RocketmqExecutionLog mqLog = new RocketmqExecutionLog();
        rocketMQTemplate.asyncSend(topic, contentMap, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                log.info("Successfully send async message and result is {} ",sendResult.getSendStatus());
                mqLog.convert(sendResult);
                mqLog.setMqMessage(contentMap);
                mqLog.setMessageType(1);
                mqLog.setTopic(topic);
                mqLog.setLogTime(new Date());
                logDao.save(mqLog);
            }

            @Override
            public void onException(Throwable throwable) {
                log.error("Async message send failed",throwable);
                mqLog.setId(UuidTool.getUUID());
                mqLog.setExecuteStatus("Failed!");
                mqLog.setTopic(topic);
                mqLog.setInformation(throwable.getMessage());
                mqLog.setMessageType(1);
                mqLog.setLogTime(new Date());
                logDao.save(mqLog);
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
        Assert.hasText(topic,"topic cannot be null");
        Assert.notNull(contentMap,"message map cannot be empty!");
        RocketmqExecutionLog mqLog = new RocketmqExecutionLog();
        try {
            SendResult result = rocketMQTemplate.syncSend(topic, MessageBuilder.withPayload(contentMap).build(), 2000, delayLevel);
            log.info("Successfully send delay message!");
            mqLog.convert(result);
            mqLog.setMqMessage(contentMap);
            mqLog.setMessageType(2);
            mqLog.setTopic(topic);
            mqLog.setLogTime(new Date());
            logDao.save(mqLog);
        }
        catch (Exception e){
            log.error("delay message send failed",e);
            mqLog.setId(UuidTool.getUUID());
            mqLog.setExecuteStatus("Failed!");
            mqLog.setTopic(topic);
            mqLog.setInformation(e.getMessage());
            mqLog.setMessageType(2);
            mqLog.setLogTime(new Date());
            logDao.save(mqLog);
        }
    }

    /**
     * Sync message sending process
     * @param topic
     * @param contentMap
     * @return sendResult
     */
    public SendResult sendMessage(String topic, Map<String,Object>contentMap){
        Assert.hasText(topic,"topic cannot be null");
        Assert.notNull(contentMap,"message map cannot be empty!");
        RocketmqExecutionLog mqLog = new RocketmqExecutionLog();
        SendResult result = rocketMQTemplate.syncSend(topic, contentMap);
        mqLog.convert(result);
        mqLog.setMqMessage(contentMap);
        mqLog.setMessageType(0);
        mqLog.setTopic(topic);
        mqLog.setLogTime(new Date());
        logDao.save(mqLog);
        return result;
    }
}
