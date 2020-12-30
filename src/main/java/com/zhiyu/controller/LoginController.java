package com.zhiyu.controller;

import com.zhiyu.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping(value = "/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/loginAuth")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        RedirectAttributes attributes,
                        HttpSession session) {
        // 获取当前用户
        Subject subject = SecurityUtils.getSubject();
        // 封装用户的数据
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        // 执行登录的方法
        try {
            subject.login(token);
            return "redirect:/admin/dashboard";
        } catch (UnknownAccountException e) {
            attributes.addFlashAttribute("message", "用户名错误");
            return "redirect:/login";
        } catch (IncorrectCredentialsException e) {
            attributes.addFlashAttribute("message", "密码错误");
            return "redirect:/login";
        }
    }

    @GetMapping("/logout")
    public String logout(RedirectAttributes attributes) {
        Subject subject = SecurityUtils.getSubject();
        //注销
        subject.logout();
        attributes.addFlashAttribute("warn", "注销成功");
        return "redirect:/login";
    }
}
