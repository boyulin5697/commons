package com.by.commons.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.util.Assert;

@Data
public class QueryEvt {
    @ApiModelProperty(value = "页数", required = false)
    private Integer queryPage = 1;
    @ApiModelProperty(value = "查询条数", required = false)
    private Integer querySize = 10;

    public void verify(){
        Assert.isTrue(querySize<200,"The query data for one request cannot larger than 200");
    }
}
