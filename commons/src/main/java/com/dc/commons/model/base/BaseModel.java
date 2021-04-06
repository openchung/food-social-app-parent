package com.dc.commons.model.base;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 實體對象公共屬性
 */
@Getter
@Setter
public class BaseModel implements Serializable {

    private Integer id;
    private Date createDate;
    private Date updateDate;
    private int isValid;

}