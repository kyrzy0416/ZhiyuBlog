package com.zhiyu.controller;

import com.zhiyu.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private BlogService blogService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("blogs", blogService.listRecommendBlogTop(3));
        return "home";
    }
}
