package com.zhiyu.controller.admin;

import com.zhiyu.domain.Tag;
import com.zhiyu.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class TagsController {

    @Autowired
    private TagService tagService;

    /**
     * 跳转到标签页并获取标签列表
     * @param pageable
     * @param model
     * @return
     */
    @GetMapping("/tags")
    public String list(@PageableDefault(size = 5, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
                       Model model) {
        model.addAttribute("page", tagService.listTag(pageable));
        return "admin/tags";
    }

    /**
     * 跳转到标签添加页面
     * @param model
     * @return
     */
    @GetMapping("/tags/input")
    public String input(Model model) {
        model.addAttribute("tag", new Tag());
        return "admin/tags-input";
    }

    /**
     * 根据id获取标签
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/tags/{id}/input")
    public String editInput(@PathVariable Long id, Model model) {
        model.addAttribute("tag", tagService.getTag(id));
        return "admin/tags-input";
    }

    /**
     * 添加标签、验证
     * @param tag
     * @param bindingResult
     * @param attributes
     * @return
     */
    @PostMapping("/tags")
    public String input(@Valid Tag tag, BindingResult bindingResult, RedirectAttributes attributes) {
        // 如果有捕获到参数不合法：Tag
        Tag tag1 = tagService.getTagByName(tag.getName());
        if (tag1 != null) {
            bindingResult.rejectValue("name", "nameError", "不能添加重复的标签");
        }
        if (bindingResult.hasErrors()) {
            return "admin/tags-input";
        }
        Tag t = tagService.saveTag(tag);
        if (t != null) {
            attributes.addFlashAttribute("message", "添加成功");
        } else {
            attributes.addFlashAttribute("message", "添加失败");
        }
        return "redirect:/admin/tags";
    }

    /**
     * 更新标签
     * @param tag
     * @param bindingResult
     * @param id
     * @param attributes
     * @return
     */
    @PostMapping("/tags/{id}")
    public String editTag(@Valid Tag tag, BindingResult bindingResult,@PathVariable Long id, RedirectAttributes attributes) {
        Tag tag1 = tagService.getTagByName(tag.getName());
        if (tag1 != null) {
            bindingResult.rejectValue("name", "nameError", "不能添加重复的标签");
        }
        // 如果有捕获到参数不合法：Tag
        if (bindingResult.hasErrors()) {
            return "admin/tags-input";
        }
        Tag t = tagService.updateTag(id, tag);
        if (t != null) {
            attributes.addFlashAttribute("message", "更新成功");
        } else {
            attributes.addFlashAttribute("message", "更新失败");
        }
        return "redirect:/admin/tags";
    }

    @GetMapping("/tags/{id}/delete")
    public String deleteTag(@PathVariable Long id, RedirectAttributes attributes) {
        tagService.deleteTag(id);
        attributes.addFlashAttribute("message", "删除成功");
        return "redirect:/admin/tags";
    }
}
