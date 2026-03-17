package com.example.demo.modules.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.modules.admin.dto.AdminDTO;
import com.example.demo.modules.admin.entity.Admin;
import com.example.demo.modules.user.dto.UserDTO;
import com.example.demo.modules.user.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * mybatis数据层
 *
 * @author zhuhuix
 * @date 2020-07-23
 */
@Repository
@Mapper
public interface AdminMapper extends BaseMapper<Admin> {
    Admin findByName(String adminName);
}
