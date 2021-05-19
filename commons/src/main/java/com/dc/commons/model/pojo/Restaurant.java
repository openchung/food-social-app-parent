package com.dc.commons.model.pojo;

import com.dc.commons.model.base.BaseModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Restaurant extends BaseModel {

    @ApiModelProperty("英文名稱")
    private String name;
    @ApiModelProperty("中文名稱")
    private String cnName;
    @ApiModelProperty("緯度")
    private Float x;
    @ApiModelProperty("經度")
    private Float y;
    @ApiModelProperty("位置-英文")
    private String location;
    @ApiModelProperty("位置-中文")
    private String cnLocation;
    @ApiModelProperty("商圈")
    private String area;
    @ApiModelProperty("電話")
    private String telephone;
    @ApiModelProperty("郵箱")
    private String email;
    @ApiModelProperty("官網")
    private String website;
    @ApiModelProperty("菜系")
    private String cuisine;
    @ApiModelProperty("均價，不顯示具體金額")
    private String averagePrice;
    @ApiModelProperty("介紹")
    private String introduction;
    @ApiModelProperty("縮略圖")
    private String thumbnail;
    @ApiModelProperty("喜歡")
    private int likeVotes;
    @ApiModelProperty("不喜歡")
    private int dislikeVotes;
    @ApiModelProperty("城市")
    private Integer cityId;

}