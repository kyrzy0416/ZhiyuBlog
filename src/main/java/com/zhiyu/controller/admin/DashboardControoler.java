package com.zhiyu.controller.admin;

import com.zhiyu.component.Log;
import com.zhiyu.service.BlogService;
import com.zhiyu.service.CommentService;
import com.zhiyu.service.ISysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class DashboardControoler {

    @Autowired
    private BlogService blogService;

    @Autowired
    private ISysLogService sysLogService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/dashboard")
    @Log("登录后台")
    public String Dashboard(@PageableDefault(size = 8, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                            Model model) {
        // 获取当前用户
        model.addAttribute("recommendBlogs", blogService.listRecommendBlogTop(8));
        model.addAttribute("pageCount", blogService.countBlog());
        model.addAttribute("commentsCount", commentService.countComments());
        model.addAttribute("views", blogService.getAllViews());
        model.addAttribute("operations", sysLogService.listSysLog());
        return "admin/dashboard";
    }
}
