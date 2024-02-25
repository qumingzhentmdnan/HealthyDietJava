package com.fjut.healthydietapp.util;


import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

//调用短信接口
@Component
public class MessageUtil {


//Component是把普通类在spring加载时候加载成bean，这时候 redisTemplate 还未加载，自动注入的是空指针了
//    @Autowired
//    private RedisTemplate redisTemplate;

    private static RedisTemplate redisTemplate;

    @Autowired
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public Result getCode(String phone) {
        String resCode = phone + ":code";
        String resCount = phone + ":count";

        //判断是否需要发送验证码
        if (redisTemplate.hasKey(resCode)) {
            return Result.error().message("验证码已发送，请稍后再试");
        }

        if (redisTemplate.hasKey(resCount)) {
            long count = (long) redisTemplate.opsForValue().get(resCount);
            if (count >= 1) {
                return Result.error().message("今日发送次数已达上限,请24小时后再试");
            }
        }

        //请求信息
        String host = "https://dfsns.market.alicloudapi.com";
        String path = "/data/send_sms";
        String method = "POST";
        String appcode = "d754ad9052544cffade21ff748aa096f";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        Map<String, String> querys = new HashMap<String, String>();
        Map<String, String> bodys = new HashMap<String, String>();

        //获取验证码
        Random random = new Random();
        int code = random.nextInt(1000, 9999);

        //将验证码存入redis
        redisTemplate.opsForValue().set(resCode, String.valueOf(code));
        redisTemplate.expire(resCode, 10, TimeUnit.MINUTES);

        //设置用户发送验证码次数
        if (!redisTemplate.hasKey(resCount)) {
            redisTemplate.opsForValue().set(resCount, "1");
            redisTemplate.expire(resCount, 1, TimeUnit.DAYS);
        } else
            redisTemplate.opsForValue().increment(resCount, 1);


        bodys.put("content", "code:" + code+",expire_at:10");
        bodys.put("template_id", "CST_qozfh101");  //该模板为调试接口专用，短信下发有受限制，调试成功后请联系客服报备专属模板
        bodys.put("phone_number", phone);


        try {
            /**
             * 重要提示如下:
             * HttpUtils请从
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             * 下载
             *
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            System.out.println(response.toString());
            //获取response的body
            //System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.ok().message("验证码发送成功");
    }
}
