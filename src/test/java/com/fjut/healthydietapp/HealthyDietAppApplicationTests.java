package com.fjut.healthydietapp;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;



@SpringBootTest
class HealthyDietAppApplicationTests {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    void contextLoads() throws InterruptedException {
        String text="hello\nworld";
        String n="\n";
        System.out.println("text = " + text);
        String res=text.replace(n,"");
        System.out.println("res = " + res);
        String res1=text.replace("h","");
        System.out.println("res1 = " + res1);
        String string = text.replaceAll("\\\\n", "");
        System.out.println("string = " + string);
    }

}
