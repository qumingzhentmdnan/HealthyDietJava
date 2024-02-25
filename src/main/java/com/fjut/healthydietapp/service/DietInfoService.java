package com.fjut.healthydietapp.service;

import com.fjut.healthydietapp.entity.DietInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fjut.healthydietapp.mapper.DietInfoMapper;
import com.fjut.healthydietapp.util.Result;
import com.fjut.healthydietapp.vo.ExportDietInfoVo;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ye
 * @since 2024年02月08日
 */
public interface DietInfoService extends IService<DietInfo> {
     List<ExportDietInfoVo> getRecordsByDate(Integer id);
}
