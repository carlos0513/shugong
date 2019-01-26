package com.frontend.core.scheduled;

import com.frontend.core.exception.GunsException;
import com.frontend.modular.dataCenter.service.BatchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@Configuration
@EnableScheduling
@Slf4j
public class QuartzService {

    @Autowired
    BatchService batchService;

    //每5分钟启动
//    @Scheduled(cron = "")
    @Scheduled(initialDelay=1000, fixedDelay=50000)
    public void httpRequestDataTask() throws IOException,GunsException {
//        batchService.sevrScheduledDataCenter();
        log.info("每次执行时间:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    }

}
