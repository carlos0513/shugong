package com.frontend.modular.system.controller;

import com.frontend.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.frontend.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.frontend.modular.system.model.User;
import com.frontend.modular.system.service.IUserService;

/**
 * 控制器
 */
@Controller
@RequestMapping("/user")
public class UserController extends BaseController {

    private String PREFIX = "/system/user/";

    @Autowired
    private IUserService userService;

    /**
     * 跳转到首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "user.html";
    }

    /**
     * 跳转到添加
     */
    @RequestMapping("/user_add")
    public String userAdd() {
        return PREFIX + "user_add.html";
    }

    /**
     * 跳转到修改
     */
    @RequestMapping("/user_update/{userId}")
    public String userUpdate(@PathVariable Integer userId, Model model) {
        User user = userService.selectById(userId);
        model.addAttribute("item",user);
        LogObjectHolder.me().set(user);
        return PREFIX + "user_edit.html";
    }

    /**
     * 获取列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return userService.selectList(null);
    }

    /**
     * 新增
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(User user) {
        userService.insert(user);
        return SUCCESS_TIP;
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer userId) {
        userService.deleteById(userId);
        return SUCCESS_TIP;
    }

    /**
     * 修改
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(User user) {
        userService.updateById(user);
        return SUCCESS_TIP;
    }

    /**
     * 详情
     */
    @RequestMapping(value = "/detail/{userId}")
    @ResponseBody
    public Object detail(@PathVariable("userId") Integer userId) {
        return userService.selectById(userId);
    }
}
