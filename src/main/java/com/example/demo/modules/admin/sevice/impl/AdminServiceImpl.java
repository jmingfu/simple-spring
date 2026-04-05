package com.example.demo.modules.admin.sevice.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.exception.ReturnException;
import com.example.demo.util.MD5Util;
import com.example.demo.config.DefaultPasswordProperties;
import com.example.demo.modules.admin.dto.AdminDTO;
import com.example.demo.modules.admin.entity.Admin;
import com.example.demo.modules.admin.mapper.AdminMapper;
import com.example.demo.modules.admin.sevice.AdminService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 基于SpringMVC框架开发web应用--管理员服务层
 *
 * @author zhuhuix
 * @date 2020-07-08
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {
    private final Logger logger = LoggerFactory.getLogger(Logger.class);
    @Autowired
    private DefaultPasswordProperties defaultPasswordProperties;

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    // 保存管理员
    @Override
    public AdminDTO saveAdmin(Admin admin) {
        Admin byName = adminMapper.findByName(admin.getUserName());
        if (byName != null) {
            throw new ReturnException("用户名已存在");
        }
        // 如果注册密码为空，则赋值默认密码
        if (StringUtils.isEmpty(admin.getUserPassword())) {
            admin.setUserPassword(defaultPasswordProperties.getPassword());
        }
//        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        admin.setUserPassword(MD5Util.encode(admin.getUserPassword()));
        if (!save(admin)) {
            throw new ReturnException("数据新增失败");
        }
        AdminDTO adminDTO = new AdminDTO();
        BeanUtils.copyProperties(admin, adminDTO);
        //baseMapper.insert(admin);
        return adminDTO;
    }

    @Override
    public AdminDTO login(Admin admin) throws Exception{
        if (StringUtils.isEmpty(admin.getUserName()) || StringUtils.isEmpty(admin.getUserPassword())) {
            throw new ReturnException("请输入用户名和密码");
        }
        //先对前端传入的明文进行加密
        admin.setUserPassword(MD5Util.encode(admin.getUserPassword()));
        Admin savedAdmin = adminMapper.findByName(admin.getUserName());
        if (savedAdmin == null) {
            throw new ReturnException("管理员不存在");
        }
        if (!admin.getUserPassword().equals(savedAdmin.getUserPassword())) {
            throw new ReturnException("密码错误，请重新输入");
        }
        AdminDTO dto = new AdminDTO();
        BeanUtils.copyProperties(savedAdmin, dto);
        String token = UUID.randomUUID().toString().replace("-", "");
        dto.setToken(token);
        redisTemplate.opsForValue().set("LOGIN:" + token, objectMapper.writeValueAsString(dto), 2, TimeUnit.HOURS);
        return dto;
    }

    @Override
    public void logout(HttpServletRequest request) {
        String token = request.getHeader("token");
        redisTemplate.delete("LOGIN_TOKEN:" + token);
    }
}
