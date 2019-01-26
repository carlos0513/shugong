package com.frontend.core.listener;

import com.frontend.modular.dataCenter.service.BatchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;  
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
@Slf4j
public class StartApplicationListener implements ApplicationListener<ContextRefreshedEvent>{

    @Autowired
    BatchService batchService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        log.info("启动完成，加载系统配置，初始化redis 数据...");

//        try {
//            batchService.sevrScheduledDataCenter();
//        } catch (IOException e) {
//            log.info("初始化redis 数据错误："+ e.getMessage());
//            e.printStackTrace();
//        }
        log.info("初始化redis 数据完成...");
    }
}