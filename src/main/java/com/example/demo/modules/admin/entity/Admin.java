package com.example.demo.modules.admin.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 基于SpringMVC框架开发web应用--管理员用户类
 *
 * @author zhuhuix
 * @date 2020-07-08
 */
@Entity
@TableName("admin")
@Data
public class Admin implements Serializable {
    // 管理员id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "user_password")
    private String userPassword;

    @Override
    public String toString() {
        return "Admin{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                '}';
    }
}
