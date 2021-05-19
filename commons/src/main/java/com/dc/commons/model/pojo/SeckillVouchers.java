package com.dc.commons.model.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.dc.commons.model.base.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Setter
@Getter
@ApiModel(description = "搶購代金券信息")
public class SeckillVouchers extends BaseModel {

    @ApiModelProperty("代金券外鍵")
    private Integer fkVoucherId;
    @ApiModelProperty("數量")
    private int amount;
    @ApiModelProperty("搶購開始時間")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date startTime;
    @ApiModelProperty("搶購結束時間")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date endTime;

}