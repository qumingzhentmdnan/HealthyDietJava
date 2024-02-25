package com.fjut.healthydietapp.controller;

import com.fjut.healthydietapp.config.CustomizedException;
import com.fjut.healthydietapp.entity.PersonaEnum;
import com.fjut.healthydietapp.util.ChatAiUtils;
import com.fjut.healthydietapp.util.Result;
import jakarta.servlet.http.HttpSession;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;


@RestController
@RequestMapping(("/chat"))
public class ChatAiController {
    private final OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder().build();

    //获取对话信息
    @GetMapping("/getChat")
    public Result getChat(Integer id,String message) throws IOException {
        //信息验证
        if(message.length()>50){
            return Result.error().message("输入的对话内容过长");
        }
        ChatAiUtils chatAiUtils = new ChatAiUtils();
        PersonaEnum personaEnum = getPersonaEnum(id);
        if(personaEnum == null) {
            return Result.error().message("对话角色不存在");
        }

        //调用工具类，获取返回对话信息
        String chatMessage = chatAiUtils.getChatMessage(personaEnum, message);
        System.out.println(chatMessage);
        String n="\n";
        chatMessage=chatMessage.replace(n,"");
        chatMessage=chatMessage.replace("/*","");
        chatMessage=chatMessage.replace("[a-zA-z0-9]","");
        System.out.println(chatMessage);
        return Result.ok().data("message",chatMessage);
    }

    //将文本信息转为语音返回
    @GetMapping(value="/getVoice")
    public ResponseEntity<byte[]> getVoice(Integer id,String message) throws IOException {
        //信息验证
        if(message.length()>500){
            throw new CustomizedException(20001,"输入的对话内容过长");
        }
        PersonaEnum personaEnum = getPersonaEnum(id);
        if(personaEnum == null) {
            throw new CustomizedException(20001,"对话角色不存在");
        }

        //拼接url
        StringBuilder url = new StringBuilder("http://region-3.seetacloud.com:30845/voice")//访问路径
                //拼接参数
                .append("?text=")
                .append(message)
                .append("&model_id=")
                .append(id)
                .append("&speaker_name=")
                .append(personaEnum.getName())
                //避免麻烦，以下参数写死，不做修改
                .append("&sdp_ratio=0.2&noise=0.2&noisew=0.9&length=1&language=ZH&auto_translate=false&auto_split=true&emotion=Sad&style_weight=0.7");

        //请求语音
        Request request = new Request.Builder()
                .url(url.toString())
                .method("GET",null)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        Response res = HTTP_CLIENT.newCall(request).execute();
        byte[] bytes = res.body().bytes();

        HttpHeaders headers = new HttpHeaders();
        String fileName = UUID.randomUUID() + ".wav";
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+fileName);
        headers.add(HttpHeaders.CONTENT_TYPE, "audio/x-wav");
        System.out.println("获取下载");
        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }



    //根据id选择不同的角色
    private PersonaEnum getPersonaEnum(Integer id) {
        //根据id选择不同的角色
        PersonaEnum personaEnum;
        switch (id){
            case 0:
                personaEnum=PersonaEnum.NingGuang;
                break;
            case 1:
                personaEnum=PersonaEnum.NaWeiLaiTe;
                break;
            case 2:
                personaEnum=PersonaEnum.BaChongShengZi;
                break;
            case 3:
                personaEnum=PersonaEnum.FuNingNa;
                break;
            default:
                return null;
        }
        return personaEnum;
    }
}