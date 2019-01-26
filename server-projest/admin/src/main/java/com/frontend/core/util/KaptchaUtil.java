package com.frontend.core.util;

import com.frontend.config.properties.AdminPropertie;

/**
 * 验证码工具类
 */
public class KaptchaUtil {

    /**
     * 获取验证码开关
     */
    public static Boolean getKaptchaOnOff() {
        return SpringContextHolder.getBean(AdminPropertie.class).getKaptchaOpen();
    }
}