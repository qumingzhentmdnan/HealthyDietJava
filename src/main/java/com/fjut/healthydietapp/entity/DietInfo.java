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
@TableName("diet_info")
public class DietInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 该条记录的用户id、
     */
    @TableField("user_id")
    private Integer userId;

    /**
     * 饮食类型
     */
    @TableField("type")
    private Integer type;

    /**
     * 食物名称
     */
    @TableField("food_name")
    private String foodName;

    /**
     * 总的卡路里
     */
    @TableField("total_calorie")
    private Double totalCalorie;

    /**
     * 份数
     */
    @TableField("count")
    private Integer count;

    @TableField("weight")
    private String weight;
    /**
     * 创造时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;

    /**
     * 逻辑删除，0为未删除
     */
    @TableLogic
    @TableField("is_deleted")
    private Byte isDeleted;
}
