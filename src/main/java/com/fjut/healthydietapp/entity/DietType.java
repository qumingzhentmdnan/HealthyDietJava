package com.fjut.healthydietapp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

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
@TableName("diet_type")
public class DietType implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("type_name")
    private String typeName;

    @TableField("suggest_lowest")
    private String suggestLowest;

    @TableField("suggest_highest")
    private String suggestHighest;

    @TableField("is_deleted")
    private String isDeleted;
}
