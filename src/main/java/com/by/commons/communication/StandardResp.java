package com.by.commons.communication;

import com.by.commons.consts.ResponseCodeEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Standard Resp
 *
 * @author by.
 * @date 2022/4/28
 */
@Data
public class StandardResp<T> implements Serializable {

    /**
     * Response code
     */
    private Integer code;
    /**
     * response message
     */
    private String message;
    /**
     * response time
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone= "GMT+8" )
    private Date time;

    /**
     * response data
     */
    private T data;

    public StandardResp error() {
        this.setCode(ResponseCodeEnum.INTERNAL_ERROR);
        this.setMessage("操作失败");
        Date date = new Date();
        this.setTime(date);
        return this;
    }

    public StandardResp error(String respMsg) {
        this.setCode(500);
        this.setMessage(respMsg);
        this.setData(null);
        Date date = new Date();
        this.setTime(date);
        return this;
    }

    public StandardResp error(int respCode, String respMsg) {
        this.setCode(respCode);
        this.setMessage(respMsg);
        this.setData(null);
        Date date = new Date();
        this.setTime(date);
        return this;
    }

    public boolean success() {
        return this.getCode() == 200 ? true : false;
    }

    public boolean hasRecord() {
        return success() && this.data != null ? true : false;
    }

    public StandardResp success(T data, String respMsg) {
        this.setCode(ResponseCodeEnum.SUCCESS);
        this.setMessage(respMsg);
        this.setData(data);
        Date date = new Date();
        this.setTime(date);
        return this;
    }

    public StandardResp success(T data) {
        this.setCode(ResponseCodeEnum.SUCCESS);
        this.setMessage("操作成功");
        this.setData(data);
        Date date = new Date();
        this.setTime(date);
        return this;
    }


    @Override
    public String toString() {
        return "ServiceResp{" +
                "message: "+message+","
                +"code: "+code+","
                +"data: "+data+","
                +"time: "+time+
                '}';
    }
}
