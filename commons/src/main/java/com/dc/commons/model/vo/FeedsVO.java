package com.dc.commons.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@ApiModel(description = "Feed顯示信息")
public class FeedsVO implements Serializable {

    @ApiModelProperty("主鍵")
    private Integer id;
    @ApiModelProperty("內容")
    private String content;
    @ApiModelProperty("點讚數")
    private int praiseAmount;
    @ApiModelProperty("評論數")
    private int commentAmount;
    @ApiModelProperty("餐廳")
    private Integer fkRestaurantId;
    @ApiModelProperty("用戶ID")
    private Integer fkDinerId;
    @ApiModelProperty("用戶信息")
    private ShortDinerInfo dinerInfo;
    @ApiModelProperty("顯示時間")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    public Date createDate;

}