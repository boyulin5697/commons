package com.by.commons.consts;

/**
 * Response code enum
 *
 * @author by.
 * @date 2022/4/28
 */
public interface ResponseCodeEnum {
    int SUCCESS = 200;
    int NO_AUTH = 403;
    int INTERNAL_ERROR = 500;
    int GATEWAY_ERROR = 502;
    /**
     * Use this code when request param has logic problems.
     */
    int REQUEST_ERROR = 503;
}
