package com.example.demo.modules.admin.sevice.impl;

import com.alibaba.druid.support.json.JSONUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.common.ReturnException;
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
import org.springframework.stereotype.Service;

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
    private AdminRepository adminRepository;

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;




    // 保存管理员
    @Override
    public boolean saveAdmin(Admin admin) {
        //baseMapper.insert(admin);
        return save(admin);
    }

    @Override
    public AdminDTO login(Admin admin) {
        Admin savedAdmin = adminMapper.findByName(admin.getUserName());
        if(savedAdmin==null){
            throw new ReturnException("管理员不存在");
        }
        if(!admin.getPassword().equals(savedAdmin.getPassword())){
            throw new ReturnException("密码错误，请重新输入");
        }
        AdminDTO dto = new AdminDTO();
        BeanUtils.copyProperties(savedAdmin,dto);
        String token = UUID.randomUUID().toString().replace("-", "");
        redisTemplate.opsForValue().set("LOGIN:"+token, JSONUtils.toJSONString(dto),2, TimeUnit.HOURS);
        dto.setToken(token);
        return dto;
    }
}
