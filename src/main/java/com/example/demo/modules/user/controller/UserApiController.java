package com.example.demo.modules.user.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import com.example.demo.modules.user.dto.UserDTO;
import com.example.demo.modules.user.service.UserService;
import com.example.demo.modules.user.entity.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 基于SpringMVC框架开发web应用--用户restful api
 * 增加、删除、修改、查找用户信息的API交互服务
 *
 * @author zhuhuix
 * @date 2020-07-10
 */
@RestController
@RequestMapping("/api/user")
@Api(tags = "用户管理模块")
public class UserApiController {
    @Autowired
    private UserService userService;

    @PostMapping
    @ApiOperation(value = "增加用户信息")
    public Result<UserDTO> addUser(@RequestBody User user) {
        return Result.success(userService.saveUser(user));
    }

    @DeleteMapping("")
    @ApiOperation(value = "根据id删除用户")
    public Result<Integer> deleteUser(@RequestParam Long id) {
        return Result.success(userService.deleteById(id));
    }

    @PutMapping("")
    @ApiOperation(value = "根据id修改用户")
    public Result<UserDTO> updateUser(@RequestBody User user) {
        return Result.success(userService.saveUser(user));
    }

    @GetMapping("")
    @ApiOperation(value = "根据id查找用户")
    public Result<UserDTO> findUser(@RequestParam Long id) {
        return Result.success(userService.findUser(id));
    }

    @GetMapping("/page")
    @ApiOperation(value = "条件分页查询")
    public Result<Page<User>> userPage(UserDTO dto) {
        return Result.success(userService.selectPage(dto));
    }
}
