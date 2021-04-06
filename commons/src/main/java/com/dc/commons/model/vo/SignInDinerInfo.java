package com.dc.commons.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@ApiModel(value = "SignInDinerInfo", description = "登錄使用者資訊")
public class SignInDinerInfo implements Serializable {

    @ApiModelProperty("主鍵")
    private Integer id;
    @ApiModelProperty("使用者名稱")
    private String username;
    @ApiModelProperty("昵稱")
    private String nickname;
    @ApiModelProperty("手機號")
    private String phone;
    @ApiModelProperty("電子信箱")
    private String email;
    @ApiModelProperty("頭像")
    private String avatarUrl;
    @ApiModelProperty("角色")
    private String roles;

}