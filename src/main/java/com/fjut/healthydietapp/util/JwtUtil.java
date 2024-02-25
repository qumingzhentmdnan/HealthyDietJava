package com.fjut.healthydietapp.util;

import io.jsonwebtoken.*;

import javax.crypto.SecretKey;
import java.util.Date;


public class JwtUtil {
    //设置token过期时间
    private  final  static long tokenExpiration = 24*60*60*1000;
    //new一个key
    private final static SecretKey key = Jwts.SIG.HS256.key().build();

    //将用户名转为token
    public static String createToken(String username){
        return Jwts.builder().
                subject(username).
                signWith(key).
                expiration(new Date(System.currentTimeMillis()+tokenExpiration)).
                compressWith(Jwts.ZIP.GZIP).
                compact();
    }

    //通过token获取userName
    public static String getUsername(String jws){
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(jws).getPayload().getSubject();
    }
}