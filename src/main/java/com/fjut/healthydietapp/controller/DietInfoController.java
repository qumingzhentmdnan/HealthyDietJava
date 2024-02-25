package com.fjut.healthydietapp.controller;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fjut.healthydietapp.config.CustomizedException;
import com.fjut.healthydietapp.entity.DietInfo;
import com.fjut.healthydietapp.mapper.DietTypeMapper;
import com.fjut.healthydietapp.service.DietInfoService;
import com.fjut.healthydietapp.service.DietTypeService;
import com.fjut.healthydietapp.service.UserInfoService;
import com.fjut.healthydietapp.util.ExcelReaderListener;
import com.fjut.healthydietapp.util.JwtUtil;
import com.fjut.healthydietapp.util.Result;
import com.fjut.healthydietapp.vo.ExportDietInfoVo;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ye
 * @since 2024年02月08日
 */
@RestController
@RequestMapping("/dietInfo")
public class DietInfoController {
    @Autowired
    private DietInfoService dietInfoService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private DietTypeMapper dietTypeMapper;


    //获取所有饮食信息
    @GetMapping("/getDietInfo")
    public Result getDietInfo(@RequestHeader String token) {
        String phone = JwtUtil.getUsername(token);
        List<DietInfo> list = dietInfoService.list();
        return Result.ok().data("list",list);
    }

    //将当前用户饮食信息导出为Excel
    @GetMapping("/exportDietInfo")
    public void exportDietInfo(HttpServletResponse response,@RequestHeader String token) throws IOException {
        String phone = JwtUtil.getUsername(token);
        Integer id = userInfoService.getUserInfoByPhone(phone).getId();

        List<ExportDietInfoVo> recordsByDate = dietInfoService.getRecordsByDate(id);

        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss")), "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), ExportDietInfoVo.class).sheet("饮食记录").doWrite(recordsByDate);
    }

    //将Excel饮食信息导入数据库
    @PostMapping("/importDietInfo")
    public String upload(MultipartFile file,@RequestHeader String token) throws IOException {
        String phone = JwtUtil.getUsername(token);
        Integer id = userInfoService.getUserInfoByPhone(phone).getId();
        EasyExcel.read(file.getInputStream(), ExportDietInfoVo.class, new ExcelReaderListener(dietTypeMapper,dietInfoService,id)).sheet().doRead();
        return "success";
    }

    //通过id获取饮食信息
    @GetMapping("/getDietInfoById/{id}")
    public Result getDietInfoById(@PathVariable Integer id) {
        DietInfo dietInfo = dietInfoService.getById(id);
        if(dietInfo==null){
            return Result.error().message("未找到该饮食信息");
        }
        return Result.ok().data("dietInfo",dietInfo);
    }

    //增加饮食信息
    @PostMapping("/addDietInfo")
    public Result addDietInfo(@RequestHeader String token, @RequestBody DietInfo dietInfo) {
         String phone = JwtUtil.getUsername(token);
        Integer id = userInfoService.getUserInfoByPhone(phone).getId();
        dietInfo.setUserId(id);
        if(dietInfo.getFoodName().length()>50){
            return Result.error().message("食物名称过长");
        }
        dietInfoService.save(dietInfo);
        return Result.ok().message("添加成功");
    }

    //删除饮食信息
    @DeleteMapping("/deleteDietInfo/{id}")
    public Result deleteDietInfo(@PathVariable Integer id) {
        dietInfoService.removeById(id);
        return Result.ok().message("删除成功");
    }

    //修改饮食信息
    @PutMapping("/updateDietInfo")
    public Result updateDietInfo(@RequestBody DietInfo dietInfo) {
        if(dietInfo!=null&&dietInfo.getFoodName().length()>50){
            return Result.error().message("食物名称过长");
        }
        dietInfoService.updateById(dietInfo);
        return Result.ok().message("修改成功");
    }
}
