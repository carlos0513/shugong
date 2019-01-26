package com.frontend.config.properties;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * flowable工作流的的配置
 */
@Configuration
@ConfigurationProperties(prefix = AdminFlowableProperties.GUNS_FLOWABLE_DATASOURCE)
public class AdminFlowableProperties {

    public static final String GUNS_FLOWABLE_DATASOURCE = "datacenter.flowable.datasource";

    /**
     * 数据源的链接
     */
    private String url = "jdbc:mysql://127.0.0.1:3306/data_cneter_flowable?autoReconnect=true&useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull";

    /**
     * 数据库账号
     */
    private String username;

    /**
     * 数据库密码
     */
    private String password;

    public void config(DruidDataSource dataSource) {
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
