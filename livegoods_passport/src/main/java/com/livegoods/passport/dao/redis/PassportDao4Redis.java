package com.livegoods.passport.dao.redis;

import java.util.concurrent.TimeUnit;

public interface PassportDao4Redis {
    String getValidateCode(String key);

    /**
     * 保存验证码
     * @param key
     * @param validateCode
     * @param times
     * @param unit
     */
    void setValidateCode(String key, String validateCode, long times, TimeUnit unit);
    //删除验证码
    void removeValidateCode(String key);
}
