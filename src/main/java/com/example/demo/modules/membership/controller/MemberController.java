package com.example.demo.modules.membership.controller;

import com.example.demo.common.Result;
import com.example.demo.modules.membership.dto.MemberDTO;
import com.example.demo.modules.membership.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 基于SpringBoot框架的个人练手项目-会员管理控制器
 *
 * @author JMF
 * @date 2026-04-05 15:41
 * @date 2026-04-05
 */
@RestController
@RequestMapping("/api/v1/member")
@Api(tags = "会员基础管理")
public class MemberController {
    @Autowired
    private MemberService memberService;

    @PostMapping("/login-or-register")
    @ApiOperation(value = "注册并登录")
    public Result<MemberDTO> wechatLogin(@RequestBody MemberDTO memberDTO){
        return Result.success(memberService.wxLogin(memberDTO));
    }
}
