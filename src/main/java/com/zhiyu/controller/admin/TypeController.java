package com.zhiyu.controller.admin;

import com.zhiyu.domain.Type;
import com.zhiyu.service.TypeService;
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
public class TypeController {

    @Autowired
    private TypeService typeService;

    /**
     * 跳转到类型页
     * @param pageable
     * @param model
     * @return
     */
    @GetMapping("/types")
    public String list(@PageableDefault(size = 5,
            sort = {"id"},
            direction = Sort.Direction.DESC) Pageable pageable,
                       Model model) {
        model.addAttribute("page", typeService.listType(pageable));
        return "admin/types";
    }

    /**
     * 跳转到类型添加页面
     * @param model
     * @return
     */
    @GetMapping("/types/input")
    public String input(Model model) {
        model.addAttribute("type", new Type());
        return "admin/types-input";
    }

    /**
     * 根据就id获取tag
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/types/{id}/input")
    public String editInput(@PathVariable Long id, Model model) {
        model.addAttribute("type", typeService.getType(id));
        return "admin/types-input";
    }

    /**
     * 校验和添加输入类型
     * @param type @Valid被校验对象
     * @param result 接收校验的结果
     * @param attributes
     * @return
     */
    @PostMapping("/types")
    public String input(@Valid Type type,
                        BindingResult result,
                        RedirectAttributes attributes) {
//        判断Type是否存在
        Type type1 = typeService.getTypeByName(type.getName());
        if (type1 != null) {
            result.rejectValue("name", "nameError", "不能重复添加分类");
        }
        if (result.hasErrors()) {
            return "admin/types-input";
        }
        Type t = typeService.saveType(type);
        if (t == null) {
            attributes.addFlashAttribute("message", "添加失败");
        } else {
            attributes.addFlashAttribute("message", "添加成功");
        }
        return "redirect:/admin/types";
    }

    /**
     * 更新类型
     * @param type
     * @param result
     * @param id
     * @param attributes
     * @return
     */
    @PostMapping("/types/{id}")
    public String editPost(@Valid Type type,
                        BindingResult result,
                        @PathVariable Long id,
                        RedirectAttributes attributes) {
//        判断Type是否存在
        Type type1 = typeService.getTypeByName(type.getName());
        if (type1 != null) {
            result.rejectValue("name", "nameError", "不能重复添加分类");
        }
        if (result.hasErrors()) {
            return "admin/types-input";
        }
        Type t = typeService.updateType(id, type);
        if (t == null) {
            attributes.addFlashAttribute("message", "更新失败");
        } else {
            attributes.addFlashAttribute("message", "更新成功");
        }
        return "redirect:/admin/types";
    }

    @GetMapping("/types/{id}/delete")
    public String delete(@PathVariable Long id,
                         RedirectAttributes attributes) {
        typeService.deleteType(id);
        attributes.addFlashAttribute("message", "删除成功");
        return "redirect:/admin/types";
    }
}
