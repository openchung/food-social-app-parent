package com.dc.commons.model.pojo;

import com.dc.commons.model.base.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel(description = "食客關注實體類")
@Getter
@Setter
public class Follow extends BaseModel {

    @ApiModelProperty("使用者ID")
    private int dinerId;
    @ApiModelProperty("關注使用者ID")
    private Integer followDinerId;

}