package com.example.demo.modules.user;
import com.example.demo.modules.admin.controller.AdminController;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

/**
 * 基于SpringMVC框架开发web应用--用户控制器
 *
 * @author zhuhuix
 * @date 2020-07-03
 * @date 2020-07-07 增加用户查找
 * @date 2020-07-23 增加修改和删除功能
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(AdminController.class);

    // 保存用户并返回到用户列表页面
    @PostMapping("/add")
    public ModelAndView saveUser(@Valid @RequestBody User user, Errors errors, Model model) {
        log.info("接收到新增用户请求，参数：{}", user); // 打印请求参数，确认是否进入方法
        userService.saveUser(user);
//        if (errors.hasErrors()) {
//            model.addAttribute("user", user);
//            if (errors.getFieldError("name") != null) {
//                model.addAttribute("nameError", errors.getFieldError("name").getDefaultMessage());
//            }
//            if (errors.getFieldError("email") != null) {
//                model.addAttribute("emailError", errors.getFieldError("email").getDefaultMessage());
//            }
//            return new ModelAndView("register", "userModel", model);
//        }

        //重定向到list页面
        return new ModelAndView("redirect:/user-list.html");
    }

    // 获取用户表单页面
    @GetMapping("/form")
    public ModelAndView createForm(Model model, @RequestParam(defaultValue = "0") Long id) {
        if (id > 0) {
            model.addAttribute("user", userService.findUser(id));
        } else {
            model.addAttribute("user", new User());
        }
        return new ModelAndView("register", "userModel", model);
    }

    // 获取用户列表页面
    @GetMapping("/list")
    public ModelAndView list(Model model) {
        log.info("接收到获取用户列表请求，参数：{}", model); // 打印请求参数，确认是否进入方法
        model.addAttribute("userList", userService.listUsers());
        return new ModelAndView("redirect:/user-list.html", "userModel", model);
    }

    // 查找输入页面
    @GetMapping("/index")
    public ModelAndView index(Model model) {
        model.addAttribute("user", new User());
        return new ModelAndView("redirect:/index.html", "userModel", model);
    }

    // 查找提交并跳转用户列表
    @PostMapping("/search")
    public ModelAndView search(@ModelAttribute User user, Model model) {
        model.addAttribute("userList", userService.searchUser(user.getName()));
        return new ModelAndView("redirect:/user-list.html", "userModel", model);
    }

    // 删除用户
    @RequestMapping(path = "/del")
    public ModelAndView del(@RequestParam(name = "id") Long id) {
        userService.deleteUser(id);
        return new ModelAndView("redirect:/index.html");

    }

}
