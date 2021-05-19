package com.dc.diners.mapper;

import com.dc.commons.model.dto.DinersDTO;
import com.dc.commons.model.pojo.Diners;
import com.dc.commons.model.vo.ShortDinerInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface DinersMapper {

    /**
     * 根據手機號查詢食客的資訊
     * @param phone
     * @return
     */
    @Select("select id, username, phone, email, is_valid " +
            " from t_diners where phone = #{phone}")
    Diners selectByPhone(@Param("phone") String phone);

    /**
     * 根據使用者查詢食客資訊
     */
    @Select("select id, username, phone, email, is_valid " +
            " from t_diners where username = #{username}")
    Diners selectByUsername(@Param("username") String username);

    /**
     *
     * @param dinersDTO
     * @return
     */
    @Insert("insert into t_diners (username, password, phone, roles, is_valid, create_date, update_date)"
            + " values (#{username}, #{password}, #{phone}, \"ROLE_USER\", 1, now(), now() )")
    int save(DinersDTO dinersDTO);


    // 根據 ID 集合查詢多個食客信息
    @Select("<script> " +
            " select id, nickname, avatar_url from t_diners " +
            " where is_valid = 1 and id in " +
            " <foreach item=\"id\" collection=\"ids\" open=\"(\" separator=\",\" close=\")\"> " +
            "   #{id} " +
            " </foreach> " +
            " </script>")
    List<ShortDinerInfo> findByIds(@Param("ids") String[] ids);

}
