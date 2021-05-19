package com.dc.commons.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@ApiModel(description = "關注食客信息")
public class ShortDinerInfo implements Serializable {
    
    @ApiModelProperty("主鍵")
    public Integer id;
    @ApiModelProperty("昵稱")
    private String nickname;
    @ApiModelProperty("頭像")
    private String avatarUrl;
    
}