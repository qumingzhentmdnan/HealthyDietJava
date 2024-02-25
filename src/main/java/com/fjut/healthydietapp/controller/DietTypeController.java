package com.fjut.healthydietapp.controller;

import com.fjut.healthydietapp.entity.DietType;
import com.fjut.healthydietapp.service.DietTypeService;
import com.fjut.healthydietapp.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ye
 * @since 2024年02月08日
 */
@RestController
@RequestMapping("/dietType")
public class DietTypeController {
    @Autowired
    private DietTypeService dietTypeService;

    @GetMapping("/getDietType")
    public Result getDietType() {
        List<DietType> list = dietTypeService.list();
        return Result.ok().data("list",list);
    }
}
