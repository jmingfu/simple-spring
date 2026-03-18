package com.example.demo.modules.admin.sevice.impl;

import com.alibaba.druid.support.json.JSONUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.common.ReturnException;
import com.example.demo.config.DefaultPasswordProperties;
import com.example.demo.modules.admin.dto.AdminDTO;
import com.example.demo.modules.admin.entity.Admin;
import com.example.demo.modules.admin.mapper.AdminMapper;
import com.example.demo.modules.admin.repository.AdminRepository;
import com.example.demo.modules.admin.sevice.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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


    // 保存管理员
    @Override
    public AdminDTO saveAdmin(Admin admin) {
        // 如果注册密码为空，则赋值默认密码
        if (StringUtils.isEmpty(admin.getPassword())) {
            admin.setUserPassword(defaultPasswordProperties.getPassword());
        }
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        admin.setUserPassword(bCryptPasswordEncoder.encode(admin.getPassword()));
        if (!save(admin)) {
            throw new ReturnException("数据新增失败");
        }
        AdminDTO adminDTO = new AdminDTO();
        BeanUtils.copyProperties(admin, adminDTO);
        //baseMapper.insert(admin);
        return adminDTO;
    }

    @Override
    public AdminDTO login(Admin admin) {
        if (StringUtils.isEmpty(admin.getUserName()) || StringUtils.isEmpty(admin.getPassword())) {
            throw new ReturnException("请输入用户名和密码");
        }
        //先对前端传入的明文进行加密
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        admin.setUserPassword(bCryptPasswordEncoder.encode(admin.getPassword()));
        Admin savedAdmin = adminMapper.findByName(admin.getUserName());
        if (savedAdmin == null) {
            throw new ReturnException("管理员不存在");
        }
        if (!admin.getPassword().equals(savedAdmin.getPassword())) {
            throw new ReturnException("密码错误，请重新输入");
        }
        AdminDTO dto = new AdminDTO();
        BeanUtils.copyProperties(savedAdmin, dto);
        String token = UUID.randomUUID().toString().replace("-", "");
        redisTemplate.opsForValue().set("LOGIN:" + token, JSONUtils.toJSONString(dto), 2, TimeUnit.HOURS);
        dto.setToken(token);
        return dto;
    }

    @Override
    public void logout(HttpServletRequest request) {
        String token = request.getHeader("token");
        redisTemplate.delete("LOGIN_TOKEN:"+token);
    }
}
