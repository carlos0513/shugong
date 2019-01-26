package com.frontend.modular.dataCenter.service;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.frontend.config.ConstantConfig;
import com.frontend.core.base.controller.BaseController;
import com.frontend.core.common.exception.BizExceptionEnum;
import com.frontend.core.exception.GunsException;
import com.frontend.core.util.HttpClient;
import com.frontend.core.util.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class BatchService {
    @Value("${datacenter.api-url}")
    private String apiUrl;

    @Autowired
    RedisUtils redisUtils;

    /**
     * @name 批量处理 数据更新拉取存入redis
     * @return
     */
    public Object sevrScheduledDataCenter() throws IOException,GunsException{
        String url = apiUrl + "/apiList.json";
        String dataJson = "";
        dataJson = HttpClient.sendGetData(url, "");

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        if (dataJson == null) {
            log.info("初始化请求服务器错误:" + url, dataJson);
            throw new GunsException(BizExceptionEnum.REQUEST_ERRORS);
        }
        JSONObject jsonobj = JSONObject.parseObject(dataJson);
        JSONObject jsonArr = JSONObject.parseObject(jsonobj.getString("data"));

        String[] keyList = ConstantConfig.keyList;
        String[] nameList = ConstantConfig.nameList;
        int key = 1;
        redisUtils.remove("admin_api_list");
        for (int j = 0; j < keyList.length; j++) {
            JSONArray munArr = JSONArray.parseArray(jsonArr.getString(keyList[j]));
            Map arrPear = new HashMap();
            arrPear.put("id", key);
            arrPear.put("pcode", 0);
            arrPear.put("code", keyList[j]);
            arrPear.put("names", nameList[j]);
            redisUtils.lPush("admin_api_list", arrPear);
            key++;
            if(munArr == null || munArr.isEmpty() ){
                log.info( "APi请求数据错误，返回结果：第" + j + "条：" + munArr);
                continue;
            }
            for (int i = 0; i < munArr.size(); i++) {
                JSONObject o = JSONObject.parseObject(munArr.get(i).toString());
                Map arr = new HashMap();
                String httpUrl = apiUrl + o.getString("value");
                arr.put("id", key);
                arr.put("pcode", keyList[j]);
                arr.put("name", o.getString("name"));
                arr.put("url", httpUrl);
                redisUtils.lPush("admin_api_list", arr);
                key++;
                list.add(arr);
                log.info(httpUrl + "：api-lisr获取完成-------------------------------------------");
            }
        }
        for (Map val:list){
            String httpUrl = (String) val.get("url");
            String arrJson = HttpClient.sendGetData(httpUrl,"");
            if (arrJson == null || arrJson.isEmpty()) {
                log.info(httpUrl + "：请求数据错误，返回结果：" + arrJson);
                continue;
            }
            redisUtils.set(httpUrl, arrJson);
            String getRedis = (String) redisUtils.get("update_"+ httpUrl);
            //当修改数据里面没有数据时，主动保存一条原始数据
            if(getRedis == null && !"".equals(getRedis)){
                JSONObject jsonstr = JSONObject.parseObject(arrJson);
                JSONArray jsonstrArr = JSONArray.parseArray(jsonstr.getString("data"));
                Map<String, Object> arrMap = new HashMap<String, Object>();
                arrMap.put("title", jsonstr.getString("title"));
                arrMap.put("code", jsonstr.getString("code"));
                arrMap.put("message", jsonstr.getString("message"));
                arrMap.put("data", jsonAnalysis(jsonstrArr));
                String jsonString = JSONUtils.toJSONString(arrMap);
                redisUtils.set("update_"+ httpUrl, jsonString);
            }
            log.info(httpUrl + "：获取数据完成-------------------------------------------");
        }
        return  "ok";
    }

    /**
     * 解析请求数据json
     */
    public List<Object> jsonAnalysis(JSONArray jsonArr){
        List<Object> arrayList = new ArrayList<Object>();
        if(jsonArr == null || jsonArr.isEmpty()){
            return  null;
        }
        for (int i= 0; i<jsonArr.size(); i++){
            JSONObject o = JSONObject.parseObject(jsonArr.get(i).toString());
            Map<String, Object> objArr = new HashMap<String, Object>();
            objArr.put("id",i);
            if(o.getString("key") != null && !o.getString("key").isEmpty()){
                objArr.put("key",o.getString("key"));
            }
            if(o.getString("data")!= null && !o.getString("data").isEmpty() ){
                System.out.println(o);
                JSONArray jsonarr = JSONArray.parseArray(o.getString("data"));
                List<Object> jsonObj =  this.jsonObjAnalysis(jsonarr);
                objArr.put("data",jsonObj);
                arrayList.add(objArr);
            }else {
                if(o.getString("name")!= null && !o.getString("name").isEmpty() ){
                    objArr.put("name",o.getString("name"));
                }
                if(o.getString("value")!= null && !o.getString("value").isEmpty() ){
                    objArr.put("value",o.getString("value"));
                }
                if(o.getString("value1")!= null && !o.getString("value1").isEmpty() ){
                    objArr.put("value1",o.getString("value1"));
                }
                if(o.getString("underUnion")!= null && !o.getString("underUnion").isEmpty() ){
                    objArr.put("value",o.getString("underUnion"));
                }
                if(o.getString("population")!= null && !o.getString("population").isEmpty() ){
                    objArr.put("value1",o.getString("population"));
                }
                if(o.getString("pviews")!= null && !o.getString("pviews").isEmpty() ){
                    objArr.put("value",o.getString("pviews"));
                }
                if(o.getString("like_sum")!= null && !o.getString("like_sum").isEmpty() ){
                    objArr.put("value1",o.getString("like_sum"));
                }
                arrayList.add(objArr);
            }
        }
        return arrayList;
    }

    public  List <Object> jsonObjAnalysis(JSONArray jsonarr){
        List <Object> list = new ArrayList<Object>();
        for (int j = 0; j<jsonarr.size(); j++){
            JSONObject v = JSONObject.parseObject(jsonarr.get(j).toString());
            Map arr = new HashMap();
            arr.put("id",j);
            if(v.getString("name")!= null && !v.getString("name").isEmpty() ){
                arr.put("name",v.getString("name"));
            }
            if(v.getString("value")!= null && !v.getString("value").isEmpty() ){
                arr.put("value",v.getString("value"));
            }
            if(v.getString("underUnion")!= null && !v.getString("underUnion").isEmpty() ){
                arr.put("value",v.getString("underUnion"));
            }
            if(v.getString("population")!= null && !v.getString("population").isEmpty() ){
                arr.put("value1",v.getString("population"));
            }
            list.add(arr);
        }
        return  list;
    }

}
