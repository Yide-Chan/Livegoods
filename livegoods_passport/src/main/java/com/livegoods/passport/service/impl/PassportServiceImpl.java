package com.livegoods.passport.service.impl;

import com.livegoods.commons.pojo.Result;
import com.livegoods.passport.dao.mongo.PassportDao4Mongo;
import com.livegoods.passport.dao.redis.PassportDao4Redis;
import com.livegoods.passport.service.PassportService;
import com.livegoods.pojo.LoginLog;
import com.livegoods.pojo.User;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class PassportServiceImpl implements PassportService {
    @Autowired
    private PassportDao4Redis passportDao4Redis;

    @Autowired
    private PassportDao4Mongo passportDao4Mongo;

    @Value("${livegoods.passport.validateCode.prefix}")
    private String validateCodeKeyPrefix;

    private final Random random = new Random();

    /**
     * 生成验证码逻辑
     * 1、 校验手机号是否合法。
     * 2、 拼接一个redis的key, 前缀+手机号
     * 3、 访问redis。查询是否已存在验证码
     * 3.1、 如果已存在验证码，进入4节点
     * 3.2、 如果不存在验证码，进入5节点
     * 4、 删除已有验证码
     * 5、 生成新的验证码
     * 6、 保存验证码到redis， 定义有效时长为5分钟。
     * 7、 返回结果
     *
     * @param phone
     * @return
     */
    @Override
    public Result<Object> sendValidateCode(String phone) {
        if (phone.length() != 11 || !phone.matches("^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$")) {
            Result<Object> result = new Result<>();
            result.setMsg("手机号不合法");
            result.setStatus(500);
            return result;
        }
        int num = 0;
        String key = validateCodeKeyPrefix + phone;
        // 获取验证码
        // 验证码规则： 序号:验证码。 序号是2位数字，默认从01开始。 验证码是6位随机数字串。
        String validateCode = passportDao4Redis.getValidateCode(key);
        // 验证码存在
        if (StringUtils.isNotBlank(validateCode)) {
            num = Integer.parseInt(validateCode.split(":")[0]);
            passportDao4Redis.removeValidateCode(key);
        }
        // 生成随机验证码
        StringBuilder builder = new StringBuilder();
        num++;
        if (num < 10) { // 如果序号小于10， 则拼接0。
            builder.append("0");
        }
        builder.append(num); // 拼接序号
        builder.append(":"); // 拼接间隔符号
        for (int i = 0; i < 6; i++) {// 拼接随机数
            builder.append(random.nextInt(10));
        }
        validateCode = builder.toString();
        passportDao4Redis.setValidateCode(key, validateCode, 60, TimeUnit.SECONDS);

        Result<Object> result = new Result<>();
        result.setMsg("验证码已发送");
        result.setStatus(200);
        System.out.println("登录验证码已生成，验证码内容是：'"+validateCode.split(":")[1]+
                "'。验证码序号是：["+validateCode.split(":")[0]+"]，此验证码仅可使用一次，且有效期为1分钟。"+ phone);

        return result;
    }

    /**
     * 登录
     *  1、 拼接key
     *  2、 从redis查询验证码
     *  3、 比较redis中的验证码，和请求提供的password是否一致
     *  4、 维护一个LoginLog日志对象
     *  5、 保存日志数据
     *  6、 返回处理结果
     * @param username
     * @param password
     * @return
     */
    @Override
    public Result<Object> login(String username, String password) {
        username = username.split(",")[0];
        password = password.split(",")[0];
        // 定义返回结果对象
        Result<Object> result = new Result<>();
        // 创建日志对象
        LoginLog log = new LoginLog();
        Date now = new Date();
        String key = validateCodeKeyPrefix + username;
        String validateCode = passportDao4Redis.getValidateCode(key);
        if(StringUtils.isBlank(validateCode)){
            result.setStatus(500);
            result.setMsg("登录失败");

            log.setUsername(username);
            log.setCurrentTime(now);
            log.setMessage("未获取验证码");
            log.setStatus(LoginLog.ERROR);
            log.setType(LoginLog.VALIDATE_CODE);
        }else {
            validateCode = validateCode.split(":")[1];
            if(validateCode.equals(password)) {
                // 验证码正确
                result.setStatus(200);
                result.setMsg("登录成功");

                log.setUsername(username);
                log.setCurrentTime(now);
                log.setMessage("登录成功");
                log.setStatus(LoginLog.SUCCESS);
                log.setType(LoginLog.VALIDATE_CODE);

                // 可选操作，用户自动注册，及用户最近登录时间更新。
                User user = passportDao4Mongo.findByPhone(username);
                if(user == null){
                    // 新用户，自动注册
                    user = new User();
                    user.setPhone(username);
                    user.setRegisterTime(now);
                    user.setLastLoginTime(now);
                    passportDao4Mongo.insertUser(user);
                }else{
                    // 老用户，更新最近登录时间
                    user.setLastLoginTime(now);
                    passportDao4Mongo.updateUser(user);
                }
            }else {
                // 验证码错误
                result.setStatus(500);
                result.setMsg("登录失败");

                log.setUsername(username);
                log.setCurrentTime(now);
                log.setMessage("验证码错误，登录失败");
                log.setStatus(LoginLog.ERROR);
                log.setType(LoginLog.VALIDATE_CODE);
            }
            // 删除已使用的验证码
            passportDao4Redis.removeValidateCode(key);
        }
        // 记录登录日志
        passportDao4Mongo.insertPassportLog(log);
        return result;
    }
}
