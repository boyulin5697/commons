package com.by.commons.logs;

import com.by.commons.apis.ApiLog;
import com.by.commons.mongodb.StandardMongoOperations;
import org.springframework.stereotype.Repository;

/**
 * ,,,
 *
 * @author by.
 * @date 2022/8/12
 */
@Repository
public class ApiLogDao extends StandardMongoOperations<ApiLog> {
}
