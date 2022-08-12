package com.by.commons.apis;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.io.Serializable;
import java.util.Date;

/**
 * Api Log (Log when using need token operation)
 *
 *
 * @author by.
 * @date 2022/8/12
 * @since 1.0.6
 */
@Data
public class ApiLog implements Serializable {
    /**
     * Api request source
     */
    @MongoId
    private String userNo;

    /**
     * The api requested
     */
    private String requestPath;

    /**
     * The time api requested
     */
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date requestTime = new Date();

}
