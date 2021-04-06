package com.dc.oauth2.server.mapper;

import com.dc.commons.model.pojo.Diners;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 *  Diner
 * */
public interface DinersMapper {
    // 根據用戶名 or 手機號 or 郵箱查詢用戶信息
    @Select("select id, username, nickname, phone, email, " +
            "password, avatar_url, roles, is_valid from t_diners where " +
            "(username = #{account} or phone = #{account} or email = #{account})")
    Diners selectByAccountInfo(@Param("account") String account);
}
