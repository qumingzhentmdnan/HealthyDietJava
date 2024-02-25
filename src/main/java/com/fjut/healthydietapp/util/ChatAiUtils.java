package com.fjut.healthydietapp.util;

import com.fjut.healthydietapp.entity.PersonaEnum;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;


import java.io.IOException;
import java.util.concurrent.TimeUnit;


public class ChatAiUtils {
    private  final String API_KEY = "你的api_key";
    private  final String SECRET_KEY = "你的secret_key";
    private final OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder().connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)     // 设置读取超时时间为10秒
            .writeTimeout(60, TimeUnit.SECONDS)    // 设置写入超时时间为10秒;
            .build();

    /**
     * 从用户的AK，SK生成鉴权签名（Access Token）
     *
     * @return 鉴权签名（Access Token）
     * @throws IOException IO异常
     */
    private String getAccessToken() throws IOException {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "grant_type=client_credentials&client_id=" + API_KEY
                + "&client_secret=" + SECRET_KEY);
        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/oauth/2.0/token")
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        Response response = HTTP_CLIENT.newCall(request).execute();
        return new JSONObject(response.body().string()).getString("access_token");
    }

    //将用户的消息发送给百度AI进行处理，得到返回的消息
    public String getChatMessage(PersonaEnum personaEnum,String message) throws IOException {
        MediaType mediaType = MediaType.parse("application/json");

        JSONObject messageObject = new JSONObject();
        messageObject.put("role", "user");
        messageObject.put("content", message);

        JSONArray messagesArray = new JSONArray();
        messagesArray.put(messageObject);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("messages", messagesArray);

        jsonObject.put("system",personaEnum.getPersona());
        RequestBody body = RequestBody.create(mediaType, jsonObject.toString());
        try {
            Request request = new Request.Builder()
                    .url("https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/completions_pro?access_token=" + getAccessToken())
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .build();
            Response response = HTTP_CLIENT.newCall(request).execute();
            System.out.println(response);
            return new JSONObject(response.body().string()).getString("result");
        }catch (Exception exception){
            System.out.println(exception.getMessage());
            System.out.println(exception.getCause());
            System.out.println(exception.toString());
        }
        return "";
    }
}