package com.dc.commons.model.pojo;

import com.dc.commons.model.base.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(description = "餐廳評論實體類")
public class Reviews extends BaseModel {

    @ApiModelProperty("評論餐廳主鍵")
    private Integer fkRestaurantId;
    @ApiModelProperty("評論內容")
    private String content;
    @ApiModelProperty("評論食客主鍵")
    private Integer fkDinerId;
    @ApiModelProperty(value = "是否喜歡", example = "0=不喜歡，1=喜歡")
    private int likeIt;

}