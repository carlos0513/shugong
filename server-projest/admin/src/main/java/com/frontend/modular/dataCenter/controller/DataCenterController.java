package com.frontend.modular.dataCenter.controller;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.frontend.config.ConstantConfig;
import com.frontend.core.base.controller.BaseController;
import com.frontend.core.base.tips.ErrorTip;
import com.frontend.core.common.annotion.Permission;
import com.frontend.core.common.constant.Const;
import com.frontend.core.common.exception.BizExceptionEnum;
import com.frontend.core.config.JsonResult;
import com.frontend.core.exception.GunsException;
import com.frontend.core.exception.GunsExceptionEnum;
import com.frontend.core.node.JsonObjSeriver;
import com.frontend.core.util.HttpClient;
import com.frontend.core.util.RedisUtils;
import com.frontend.modular.dataCenter.service.BatchService;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.*;

@Controller
@RequestMapping("/datacenter")
@Slf4j
public class DataCenterController extends BaseController {

    private String PREFIX = "/datacenter/data/";

    @Value("${datacenter.api-url}")
    private String apiUrl;

    @Autowired
    RedisUtils redisUtils;

    @Autowired
    BatchService batchService;

    /**
     * 主页
     */
    @RequestMapping("")
    public String index() {

        return PREFIX + "index.html";
    }

    /**
     * 获取所有接口名称
     */
    @RequestMapping(value = "/list")
    @Permission(Const.ADMIN_NAME)
    @ResponseBody
    public Object list(String condition) throws IOException {

        List apiList = redisUtils.lRange("admin_api_list", 0, -1);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        if (!apiList.isEmpty()) {
            list = apiList;
        }
        return list;
    }


    @RequestMapping("/update/{id}")
    @Permission(Const.ADMIN_NAME)
    public String update(@PathVariable("id") String id, Model model) throws IOException {
        int ids = Integer.parseInt(id) - 1;
        List apijson = redisUtils.lRange("admin_api_list", ids, ids);
        Object apiurl = ((Map) apijson.get(0)).get("url");
        String dataJson = "";
        String getRedis = "";
        if (!"".equals(apiurl) || apiurl != null) {
            dataJson = HttpClient.sendGetData((String) apiurl, "");
            if (dataJson == null || dataJson.isEmpty()) {
                throw new GunsException(BizExceptionEnum.REQUEST_NULL);
            }
            redisUtils.set((String) apiurl, dataJson);
            getRedis = (String) redisUtils.get("update_"+ apiurl);
            if(getRedis == null && !"".equals(getRedis)){
                redisUtils.set("update_"+ apiurl, dataJson);
            }
        }
        log.info(dataJson);
        JSONObject jsonobj = JSONObject.parseObject(dataJson);
        JSONArray jsonArr = JSONArray.parseArray(jsonobj.getString("data"));
        List<Object> list = new ArrayList<Object>();
        List<Object> listCype = new ArrayList<Object>();
        if(getRedis!=null && !"".equals(getRedis)){
            JSONObject jsonstr = JSONObject.parseObject(getRedis);
            JSONArray jsonstrArr = JSONArray.parseArray(jsonstr.getString("data"));
            list = batchService.jsonAnalysis(jsonstrArr);
        }else{
            list = batchService.jsonAnalysis(jsonArr);
        }
        model.addAttribute("title", jsonobj.getString("title"));
        model.addAttribute("apiurl", apiurl);
        model.addAttribute("dataList", list);
        model.addAttribute("dataList2", listCype);
        return PREFIX + "update.html";
    }


    @RequestMapping("/edit")
    @Permission(Const.ADMIN_NAME)
    @ResponseBody
    public Object edit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        response.setContentType("application/x-www-form-urlencoded; charset=utf-8");
        JSONObject jsonObj = JSONObject.parseObject(request.getParameter("jsonStr"));
        JSONObject jsondata = JSONObject.parseObject(jsonObj.getString("data"));
        Integer i = 0;
        List<Object> list = new ArrayList<Object>();
        while (true) {
            JSONObject jsonArr = JSONObject.parseObject(jsondata.getString(String.valueOf(i)));
            Map arr = new HashMap();
            if (jsonArr == null || jsonArr.isEmpty()) {
                break;
            }
            if(jsonArr.getString("key") != null && !jsonArr.getString("key").isEmpty()){
                arr.put("key",jsonArr.getString("key"));
            }
            if(jsonArr instanceof JSONObject){
                int v =0;
                List valList = new ArrayList();
                while (true){
                    Map data = new HashMap();
                    JSONObject val = JSONObject.parseObject(jsonArr.getString(String.valueOf(v)));
                    if (val == null || val.isEmpty()) {
                        break;
                    }
                    if (val.getString("name") != null || !val.getString("name").isEmpty()) {
                        data.put("name", val.getString("name"));
                    }
                    if (val.getString("value") != null || !val.getString("value").isEmpty()) {
                        data.put("value", val.getString("value"));
                    }
                    valList.add(data);
                    v ++;
                }
                arr.put("data", valList);
            }else{
                if (jsonArr.getString("name") != null || !jsonArr.getString("name").isEmpty()) {
                    arr.put("name", jsonArr.getString("name"));
                }
                if (jsonArr.getString("value") != null || !jsonArr.getString("value").isEmpty()) {
                    arr.put("value", jsonArr.getString("value"));
                }
                if (jsonArr.getString("value1") != null || !jsonArr.getString("value1").isEmpty()) {
                    arr.put("value1", jsonArr.getString("value1"));
                }
            }
            list.add(arr);
            i++;
        }
        //获取原始数据信息
        String getRedis = (String) redisUtils.get(jsonObj.getString("apiurl"));
        JSONObject jsonstr = JSONObject.parseObject(getRedis);

        Map<String, Object> arrMap = new HashMap<String, Object>();
        arrMap.put("title", jsonObj.getString("title"));
        arrMap.put("code", jsonstr.getString("code"));
        arrMap.put("message", jsonstr.getString("message"));
        arrMap.put("data", list);
        String jsonString = JSONUtils.toJSONString(arrMap);
        String redisKey = "update_" + jsonObj.getString("apiurl");
        boolean isredis = redisUtils.set(redisKey, jsonString);
        if (isredis) {
            return SUCCESS_TIP;
        }
        return new ErrorTip(400, "修改数据错误");
    }

}
