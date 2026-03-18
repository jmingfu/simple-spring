package com.example.demo.modules.admin.sevice;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.modules.admin.dto.AdminDTO;
import com.example.demo.modules.admin.entity.Admin;
import com.example.demo.modules.admin.repository.AdminRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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
    AdminDTO login(Admin admin);
    //管理员登出
    void logout(HttpServletRequest request);
}
