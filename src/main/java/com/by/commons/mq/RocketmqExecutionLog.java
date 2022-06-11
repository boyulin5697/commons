package com.by.commons.mq;

import lombok.Data;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Rocketmq execution log
 *
 * @author by.
 * @date 2022/6/11
 */
@Data
public class RocketmqExecutionLog {
    /**
     * log id
     */
    @MongoId
    private String id;

    /**
     * 0.normal
     * 1.async
     * 2.delay
     */
    private int messageType;

    /**
     * rocketmq topic
     */
    private String topic;

    /**
     * two values, Success/Failed
     */
    private String executeStatus;

    /**
     * mq message, map structure
     */
    private Map<String,Object> mqMessage = new HashMap<>();

    /**
     * information, or error
     */
    private String information;

    /**
     * log time
     */
    private Date logTime;


    public void convert(SendResult result){
        if(result.getSendStatus()!= SendStatus.SEND_OK){
            this.executeStatus = "Failed";
        }
        else{
            this.executeStatus = "Success";
        }
        this.id = result.getMsgId();
    }
}
