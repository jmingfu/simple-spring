package com.example.demo.modules.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.modules.integration.RegisterMessageGateway;
import com.example.demo.modules.user.dto.UserDTO;
import com.example.demo.modules.user.entity.User;
import com.example.demo.modules.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 基于SpringMVC框架开发web应用--用户服务类
 *
 * @author zhuhuix
 * @date 2020-07-03
 * @date 2020-07-04 增加通过jdbcTemplate处理数据
 * @date 2020-07-07 将jdbcTemplate处理数据程序改为Spring Data JPA的处理方式
 * @date 2020-07-10 增加删除deleteUser和查找findUser
 * @date 2020-07-13 首次保存用户后通过邮件管理器发送通知邮件
 * @date 2020-07-14 将同步发送通知邮件的功能变更为通过消息队列异步发送
 * @date 2020-07-15 通过调用注册通知流程，屏蔽调用消息队列
 * @date 2020-07-23 将业务层UserService中的调用JPA的方法修改成新的UserMapper方法
 */
public interface UserService {
    // 返回所有的用户
    List<User> listUsers();

    // 保存用户
    UserDTO saveUser(User user);

    // 删除用户
    int deleteById(Long id);

    // 查找用户
    UserDTO findUser(Long id);

    // 根据名称查找用户
    List<User> searchUser(String name);

    Page<User> selectPage(UserDTO dto);
}
