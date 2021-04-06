package com.dc.oauth2.server.service;

import com.dc.commons.model.domain.SignInIdentity;
import com.dc.commons.model.pojo.Diners;
import com.dc.commons.utils.AssertUtil;
import com.dc.oauth2.server.mapper.DinersMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 登入驗證
 * */
@Service
public class UserService implements UserDetailsService {

    @Resource
    DinersMapper dinersMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AssertUtil.isNotEmpty(username, "請輸入使用者名稱");
        Diners diners = dinersMapper.selectByAccountInfo(username);
        if(diners == null) {
            throw new UsernameNotFoundException("使用者名稱或密碼錯誤，請重新輸入");
        }

        //初始化認證登入對象
        SignInIdentity signInIdentity = new SignInIdentity();
        //複製屬性
        BeanUtils.copyProperties(diners, signInIdentity);
        return signInIdentity;
    }
}
