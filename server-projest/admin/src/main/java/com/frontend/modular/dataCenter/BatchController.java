package com.frontend.modular.dataCenter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.frontend.config.ConstantConfig;
import com.frontend.core.base.tips.ErrorTip;
import com.frontend.core.common.exception.BizExceptionEnum;
import com.frontend.core.exception.GunsException;
import com.frontend.core.util.HttpClient;
import com.frontend.core.util.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @name 定时任务
 */
@Component
@Controller
@Slf4j
public class BatchController {

    @Value("${datacenter.api-url}")
    private String apiUrl;

    @Autowired
    RedisUtils redisUtils;

    /**
     * @name 批量处理 数据更新拉取存入redis
     * @return
     */
    public Object sevrScheduledDataCenter() {

        return  "ok";
    }
}
