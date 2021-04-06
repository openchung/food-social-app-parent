package com.dc.commons.model.domain;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
/**
 * 登錄認證對象
 */
@Getter
@Setter
public class SignInIdentity implements UserDetails {

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
    // 是否有效 0=無效 1=有效
    private int isValid;
    // 角色集合, 不能為空
    private List<GrantedAuthority> authorities;

    // 獲取角色信息
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (StrUtil.isNotBlank(this.roles)) {
            // 獲取數據庫中的角色信息
            Lists.newArrayList();
            this.authorities = Stream.of(this.roles.split(",")).map(role -> {
                return new SimpleGrantedAuthority(role);
            }).collect(Collectors.toList());
        } else {
            // 如果角色為空則設置為 ROLE_USER
            this.authorities = AuthorityUtils
                    .commaSeparatedStringToAuthorityList("ROLE_USER");
        }
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.isValid == 0 ? false : true;
    }

}