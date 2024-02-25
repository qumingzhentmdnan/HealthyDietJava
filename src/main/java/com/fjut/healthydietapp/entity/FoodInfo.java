package com.fjut.healthydietapp.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author ye
 * @since 2024年02月08日
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("food_info")
public class FoodInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 食物名字
     */
    @TableField("food_name")
    private String foodName;

    /**
     * 食物分量
     */
    @TableField("weight")
    private String weight;

    /**
     * 卡路里
     */
    @TableField("calorie")
    private Double calorie;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;

    /**
     * 是否逻辑删除，0表示未
     */
    @TableLogic
    @TableField("is_deleted")
    private Byte isDeleted;
}
