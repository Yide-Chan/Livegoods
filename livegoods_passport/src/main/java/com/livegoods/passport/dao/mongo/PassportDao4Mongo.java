package com.livegoods.passport.dao.mongo;

import com.livegoods.pojo.LoginLog;
import com.livegoods.pojo.User;

public interface PassportDao4Mongo {
    void insertPassportLog(LoginLog loginLog);
    void insertUser(User user);
    void updateUser(User user);
    User findByPhone(String phone);
}
