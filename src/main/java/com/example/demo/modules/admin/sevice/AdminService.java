package com.example.demo.modules.admin.sevice;

import com.example.demo.modules.admin.dto.AdminDTO;
import com.example.demo.modules.admin.entity.Admin;

import javax.servlet.http.HttpServletRequest;

/**
 * 基于SpringMVC框架开发web应用--管理员服务层
 *
 * @author zhuhuix
 * @date 2020-07-08
 */
public interface AdminService{

    // 管理员注册
    AdminDTO saveAdmin(Admin admin);
    // 管理员登录
    AdminDTO login(Admin admin) throws Exception;
    //管理员登出
    void logout(HttpServletRequest request);
}
