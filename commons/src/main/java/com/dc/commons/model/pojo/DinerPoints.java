package com.dc.commons.model.pojo;

import com.dc.commons.model.base.BaseModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DinerPoints extends BaseModel {

    @ApiModelProperty("關聯DinerId")
    private Integer fkDinerId;
    @ApiModelProperty("積分")
    private Integer points;
    @ApiModelProperty(name = "類型",example = "0=簽到，1=關注好友，2=添加Feed，3=添加商戶評論")
    private Integer types;

}