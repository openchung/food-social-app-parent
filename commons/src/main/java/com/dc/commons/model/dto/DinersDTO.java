package com.dc.commons.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@ApiModel(description = "注冊使用者資訊")
public class DinersDTO implements Serializable {

    @ApiModelProperty("使用者名稱")
    private String username;
    @ApiModelProperty("密碼")
    private String password;
    @ApiModelProperty("手機號")
    private String phone;
    @ApiModelProperty("驗證碼")
    private String verifyCode;

}
