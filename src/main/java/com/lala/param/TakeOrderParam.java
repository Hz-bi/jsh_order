package com.lala.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TakeOrderParam {

    @ApiModelProperty(value = "订单状态")
    private String status;

}
