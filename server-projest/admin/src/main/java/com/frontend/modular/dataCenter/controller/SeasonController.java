package com.frontend.modular.dataCenter.controller;


import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSONObject;
import com.frontend.config.ConstantConfig;
import com.frontend.core.base.controller.BaseController;
import com.frontend.core.base.tips.ErrorTip;
import com.frontend.core.common.annotion.Permission;
import com.frontend.core.common.constant.Const;
import com.frontend.core.util.RedisUtils;
import com.frontend.modular.dataCenter.model.ResultTypeDesc;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * 季节管理
 */

@Controller
@Slf4j
@RequestMapping("/season")
public class SeasonController extends BaseController {

    private String PREFIX = "/datacenter/season/";

    @Autowired
    RedisUtils redisUtils;


    @Value("${datacenter.api-url}")
    private String apiUrl;
    /**
     * 主页
     */
    @RequestMapping("")
    public String index() {

        return PREFIX + "index.html";
    }

    /**
     * 跳转到添加页面
     */
    @RequestMapping("/addweb")
    public String addweb() {
        return PREFIX + "add.html";
    }

    /**
     * 获取所有数据
     */
    @RequestMapping(value = "/list")
    @Permission(Const.ADMIN_NAME)
    @ResponseBody
    public Object list(String condition) throws IOException {
        List seasonList = redisUtils.hmvals("season_list_all");
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        if (!seasonList.isEmpty()) {
            list = seasonList;
        }
        return list;
    }

    /**
     * ajax新增
     * @param
     */
    @RequestMapping(value = "/add")
    @Permission(Const.ADMIN_NAME)
    @ResponseBody
    public Object add(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObj = JSONObject.parseObject(request.getParameter("jsonStr"));
        List<Object> list = new ArrayList<Object>();

        String[] dateTime= jsonObj.getString("dateTime").split(" - ");
        Map<String, Object> arr = new HashMap<String, Object> ();
        int id  = Integer.parseInt(jsonObj.getString("season"));
        arr.put("id",id + 1);
        arr.put("season", ConstantConfig.seasonName[id]);
        if(dateTime.length > 1){
            arr.put("startTime",dateTime[0]);
            arr.put("endTime",dateTime[1]);
        }
        arr.put("describe",jsonObj.getString("describe"));
        arr.put("entity",jsonObj.getString("entity"));
        arr.put("solarterms",jsonObj.getString("solarterms"));
        System.out.println(arr);
        redisUtils.hmSet("season_list_all", id,arr);

        Map<String, Object> arrMap = new HashMap<String, Object>();
        arrMap.put("title", "普惠大健康指数");
        arrMap.put("code", 200);
        arrMap.put("message", "");
        arrMap.put("data", list);
        String jsonString = JSONUtils.toJSONString(arrMap);
        String redisKey = "update_" + apiUrl;
//        boolean isredis = redisUtils.set(redisKey, jsonString);
        return SUCCESS_TIP;
    }

    /**
     * @name 跳转到修改页面
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/update/{id}")
    @Permission(Const.ADMIN_NAME)
    public String update(@PathVariable("id") String id, Model model) {
        int ids = Integer.parseInt(id) - 1;
        Object apijson = redisUtils.hmGet("season_list_all", ids);
        System.out.println(apijson);

        model.addAttribute("title", "普惠大健康指数");
        model.addAttribute("dataList", apijson);
        return PREFIX + "update.html";
    }

    /**
     * @name ajax修改
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping("/edit")
    @Permission(Const.ADMIN_NAME)
    @ResponseBody
    public Object edit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        response.setContentType("application/x-www-form-urlencoded; charset=utf-8");
        JSONObject jsonObj = JSONObject.parseObject(request.getParameter("jsonStr"));
        JSONObject jsondata = JSONObject.parseObject(jsonObj.getString("data"));
        Integer i = 0;
        List<Object> list = new ArrayList<Object>();

        //获取原始数据信息
        String getRedis = (String) redisUtils.get(jsonObj.getString("apiurl"));
        JSONObject jsonstr = JSONObject.parseObject(getRedis);

        Map<String, Object> arrMap = new HashMap<String, Object>();
        arrMap.put("title", jsonObj.getString("title"));
        arrMap.put("code", 200);
        arrMap.put("message", "");
        arrMap.put("data", list);
        String jsonString = JSONUtils.toJSONString(arrMap);
        String redisKey = jsonObj.getString("apiurl");
        boolean isredis = redisUtils.set(redisKey, jsonString);
        if (isredis) {
            return SUCCESS_TIP;
        }
        return new ErrorTip(400, "修改数据错误");
    }

    /**
     * @name 数据删除
     * @param id
     * @param model
     * @return
     * @throws ServletException
     * @throws IOException
     */
    @Permission(Const.ADMIN_NAME)
    @RequestMapping("/del/{id}")
    @ResponseBody
    public Object del(@PathVariable("id") String id, Model model) throws ServletException, IOException {

//            return SUCCESS_TIP;
        return new ErrorTip(400, "修改数据错误");
    }
}
