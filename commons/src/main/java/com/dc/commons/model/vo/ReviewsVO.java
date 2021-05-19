package com.dc.commons.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.dc.commons.model.pojo.Reviews;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@ApiModel(description = "餐廳評論實體類")
public class ReviewsVO extends Reviews {

    @ApiModelProperty("食客信息")
    private ShortDinerInfo dinerInfo;
    @ApiModelProperty(value = "創建日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date createDate;

}