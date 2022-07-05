package com.by.commons.logs;

import com.by.commons.mongodb.StandardMongoOperations;
import com.by.commons.mq.RocketmqExecutionLog;
import org.springframework.stereotype.Repository;

/**
 * RocketMQ dao
 *
 * @author by.
 * @date 2022/6/11
 * @since 1.0.3
 */
@Repository
public class RocketMQLogDao extends StandardMongoOperations<RocketmqExecutionLog> {


}
