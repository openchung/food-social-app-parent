package com.dc.commons.model.pojo;

import com.dc.commons.model.base.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel(description = "代金券訂單信息")
@Getter
@Setter
public class VoucherOrders extends BaseModel {

    @ApiModelProperty("訂單編號")
    private String orderNo;
    @ApiModelProperty("代金券")
    private Integer fkVoucherId;
    @ApiModelProperty("下單用戶")
    private Integer fkDinerId;
    @ApiModelProperty("生成qrcode")
    private String qrcode;
    @ApiModelProperty("支付方式 0=微信支付 1=支付寶")
    private int payment;
    @ApiModelProperty("訂單狀態 -1=已取消 0=未支付 1=已支付 2=已消費 3=已過期")
    private int status;
    @ApiModelProperty("訂單類型 0=正常訂單 1=搶購訂單")
    private int orderType;
    @ApiModelProperty("搶購訂單的外鍵")
    private int fkSeckillId;

}