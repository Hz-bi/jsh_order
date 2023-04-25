package com.lala.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@ApiModel(value = "路线参数")
public class PlaceOrderParam {

    @NotNull
    @ApiModelProperty(value = "起点经纬度")
    private List<String> origin;

    @NotNull
    @ApiModelProperty(value = "终点经纬度")
    private List<String> destination;


}
