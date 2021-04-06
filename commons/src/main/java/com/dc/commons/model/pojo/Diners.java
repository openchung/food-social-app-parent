package com.dc.commons.model.pojo;


import com.dc.commons.model.base.BaseModel;
import lombok.Getter;
import lombok.Setter;

/**
 * 食客實體類
 */
@Getter
@Setter
public class Diners extends BaseModel {

    // 主鍵
    private Integer id;
    // 用戶名
    private String username;
    // 昵稱
    private String nickname;
    // 密碼
    private String password;
    // 手機號
    private String phone;
    // 郵箱
    private String email;
    // 頭像
    private String avatarUrl;
    // 角色
    private String roles;

}