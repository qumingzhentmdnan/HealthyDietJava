package com.fjut.healthydietapp.controller;

import com.fjut.healthydietapp.service.FoodInfoService;
import com.fjut.healthydietapp.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ye
 * @since 2024年02月08日
 */
@RestController
@RequestMapping("/foodInfo")
public class FoodInfoController {
    @Autowired
    private FoodInfoService foodInfoService;

    @GetMapping("/getAll")
    public Result getAllFoodInfo(){
        return Result.ok().data("list",foodInfoService.list());
    }
}
