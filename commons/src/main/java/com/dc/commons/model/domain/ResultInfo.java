package com.dc.commons.model.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 公共返回對象
 */
@Getter
@Setter
@ApiModel(value = "返回說明")
public class ResultInfo<T> implements Serializable {

    @ApiModelProperty(value = "成功標識0=失敗，1=成功")
    private Integer code;
    @ApiModelProperty(value = "描述信息")
    private String message;
    @ApiModelProperty(value = "訪問路徑")
    private String path;
    @ApiModelProperty(value = "返回數據對象")
    private T data;

}