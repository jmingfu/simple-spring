package com.example.demo.modules.admin.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Collection;

/**
 * 基于SpringMVC框架开发web应用--管理员用户类
 *
 * @author zhuhuix
 * @date 2020-07-08
 */
@ApiModel(description = "管理员信息")
public class AdminDTO{

    @ApiModelProperty("管理员id")
    private Long id;

    @ApiModelProperty("管理员用户名")
    private String userName;

    @ApiModelProperty("管理员登录token")
    private String token;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                '}';
    }
}
