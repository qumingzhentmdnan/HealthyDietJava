package com.fjut.healthydietapp.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ExportDietInfoVo {
    private static final long serialVersionUID = 1L;
    /**
     * 饮食类型
     */
    @ExcelProperty("饮食类型")
    @ColumnWidth(20)
    private String typeName;

    /**
     * 食物名称
     */
    @ExcelProperty("食物名称")
    @ColumnWidth(20)
    private String foodName;


    /**
     * 份数
     */
    @ExcelProperty("份数")
    @ColumnWidth(20)
    private Integer count;

    /**
     * 总的卡路里
     */
    @ExcelProperty("总的卡路里")
    @ColumnWidth(20)
    private Double totalCalorie;

    /**
     * 更新时间
     */
    @ExcelProperty("更新时间")
    @DateTimeFormat("yyyy年MM月dd日HH时mm分ss秒")
    @ColumnWidth(30)
    private LocalDateTime createTime;
}