package com.by.commons.aop;

import com.by.commons.logs.RocketMQLogDao;
import com.by.commons.mq.RocketmqExecutionLog;
import com.by.commons.tools.UuidTool;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * MQ log aop handler
 *
 * @author by.
 * @date 2022/7/5
 * @since 1.0.4
 */
@Aspect
@Component
@Slf4j
public class MQLogHandler {

    @Autowired
    private RocketMQLogDao logDao;

    @Pointcut(value = "execution(* com.by.commons.mq.RocketmqProducer.sendDelayMessage(..))")
    public void MQDelayLogPointCut(){

    }

    @Pointcut(value = "execution(* com.by.commons.mq.RocketmqProducer.sendMessage(..)))")
    public void MQLogPointCut(){

    }

    /**
     * normal send log
     * @param joinPoint input
     * @param sendResult output
     */
    @AfterReturning(returning = "sendResult",pointcut = "MQLogPointCut()")
    public void normalLog(JoinPoint joinPoint,SendResult sendResult){
        Object[] params = joinPoint.getArgs();
        String topic = "";
        Map<String,Object>contentMap = new HashMap<>();
        for(Object param:params){
            if(param instanceof String) {
                topic = (String) param;
            }if(param instanceof Map){
                contentMap = (Map<String,Object>)param;
            }
        }
        if(!topic.equals("")){
            RocketmqExecutionLog log = new RocketmqExecutionLog();
            log.convert(sendResult);
            log.setTopic(topic);
            log.setMqMessage(contentMap);
            log.setMessageType(0);
            log.setLogTime(new Date());
            logDao.save(log);
        }
    }

    @AfterThrowing(throwing = "e",value = "MQLogPointCut()")
    public void normalThrowAdvice(JoinPoint joinPoint,Exception e){
        Object[] params = joinPoint.getArgs();
        String topic = "";
        Map<String,Object>contentMap = new HashMap<>();
        for(Object param:params){
            if(param instanceof String) {
                topic = (String) param;
            }if(param instanceof Map){
                contentMap = (Map<String,Object>)param;
            }
        }
        RocketmqExecutionLog mqLog = new RocketmqExecutionLog();
        mqLog.setId(UuidTool.getUUID());
        mqLog.setExecuteStatus("Failed!");
        mqLog.setTopic(topic);
        mqLog.setMqMessage(contentMap);
        mqLog.setInformation(e.getMessage());
        mqLog.setMessageType(0);
        mqLog.setLogTime(new Date());
        logDao.save(mqLog);
    }



    /**
     * normal send log
     * @param joinPoint input
     * @param sendResult output
     */
    @AfterReturning(returning = "sendResult",value = "MQDelayLogPointCut()")
    public void delayLog(JoinPoint joinPoint,SendResult sendResult){
        Object[] params = joinPoint.getArgs();
        String topic = "";
        Map<String,Object>contentMap = new HashMap<>();
        for(Object param:params){
            if(param instanceof String) {
                topic = (String) param;
            }if(param instanceof Map){
                contentMap = (Map<String,Object>)param;
            }
        }
        if(!topic.equals("")){
            RocketmqExecutionLog log = new RocketmqExecutionLog();
            log.convert(sendResult);
            log.setTopic(topic);
            log.setMqMessage(contentMap);
            log.setMessageType(2);
            log.setLogTime(new Date());
            logDao.save(log);
        }
    }

    @AfterThrowing(throwing = "e",value = "MQLogPointCut()")
    public void delayThrowAdvice(JoinPoint joinPoint,Exception e){
        Object[] params = joinPoint.getArgs();
        String topic = "";
        Map<String,Object>contentMap = new HashMap<>();
        for(Object param:params){
            if(param instanceof String) {
                topic = (String) param;
            }if(param instanceof Map){
                contentMap = (Map<String,Object>)param;
            }
        }
        RocketmqExecutionLog mqLog = new RocketmqExecutionLog();
        mqLog.setId(UuidTool.getUUID());
        mqLog.setExecuteStatus("Failed!");
        mqLog.setTopic(topic);
        mqLog.setMqMessage(contentMap);
        mqLog.setInformation(e.getMessage());
        mqLog.setMessageType(2);
        mqLog.setLogTime(new Date());
        logDao.save(mqLog);
    }

}
