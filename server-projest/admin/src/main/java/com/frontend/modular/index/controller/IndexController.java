package com.frontend.modular.index.controller;

import com.frontend.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/index")
public class IndexController extends BaseController {


    @ResponseBody
    @RequestMapping("index")
    public String index(){

        return "index/index";
    }
}
