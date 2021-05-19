package com.dc.commons.model.pojo;

import com.dc.commons.model.base.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(description = "Feed信息類")
public class Feeds extends BaseModel {

    @ApiModelProperty("內容")
    private String content;
    @ApiModelProperty("食客")
    private Integer fkDinerId;
    @ApiModelProperty("點讚")
    private int praiseAmount;
    @ApiModelProperty("評論")
    private int commentAmount;
    @ApiModelProperty("關聯的餐廳")
    private Integer fkRestaurantId;

}