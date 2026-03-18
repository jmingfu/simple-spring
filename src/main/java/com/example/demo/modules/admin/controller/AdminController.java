package com.example.demo.modules.admin.controller;

import com.example.demo.common.Result;
import com.example.demo.common.ReturnException;
import com.example.demo.config.DefaultPasswordProperties;
import com.example.demo.modules.admin.dto.AdminDTO;
import com.example.demo.modules.admin.entity.Admin;
import com.example.demo.modules.admin.sevice.AdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 基于SpringMVC框架开发web应用--管理员注册控制器
 *
 * @author zhuhuix
 * @date 2020-07-08
 * @date 2020-07-09 注册密码为空时使用自定义的默认密码属性
 */
@RestController
@RequestMapping("/admin")
@Api(tags = "管理员模块")
public class AdminController {
    @Autowired
    private AdminService adminService;

    private static final Logger log = LoggerFactory.getLogger(AdminController.class);

    @Deprecated
    @GetMapping
    @ApiOperation(value = "管理注册页(已废弃)")
    public ModelAndView registerForm(Model model) {
        model.addAttribute("admin", new Admin());
        return new ModelAndView("registration", "adminModel", model);
    }

    @PostMapping("/register")
    @ApiOperation(value = "提交注册信息")
    public Result<AdminDTO> save(@RequestBody Admin admin) {
        log.info("接收到管理员注册请求，参数：{}", admin); // 打印请求参数，确认是否进入方法

        return Result.success(adminService.saveAdmin(admin));

    }

    @PostMapping("/login")
    @ApiOperation(value = "管理员用户名密码登录")
    public Result<AdminDTO> login(@RequestBody Admin admin) {
        return Result.success(adminService.login(admin));
    }

    @PostMapping("/logout")
    @ApiOperation(value = "管理员登出")
    public Result<String> login(HttpServletRequest request) {
        adminService.logout(request);
        return Result.success("操作成功");
    }
}
