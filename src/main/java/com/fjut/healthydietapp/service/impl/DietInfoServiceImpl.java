package com.fjut.healthydietapp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fjut.healthydietapp.entity.DietInfo;
import com.fjut.healthydietapp.entity.DietType;
import com.fjut.healthydietapp.mapper.DietInfoMapper;
import com.fjut.healthydietapp.mapper.DietTypeMapper;
import com.fjut.healthydietapp.service.DietInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fjut.healthydietapp.util.Result;
import com.fjut.healthydietapp.vo.ExportDietInfoVo;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ye
 * @since 2024年02月08日
 */
@Service
public class DietInfoServiceImpl extends ServiceImpl<DietInfoMapper, DietInfo> implements DietInfoService {

    @Autowired
    private DietInfoMapper  dietInfoMapper;

    @Autowired
    private DietTypeMapper dietTypeMapper;


    @Override
    public List<ExportDietInfoVo> getRecordsByDate(Integer id) {
        QueryWrapper<DietInfo> query = new QueryWrapper<DietInfo>()
                .eq("is_deleted", 0)
                .eq("user_id", id)
                .orderByAsc("create_time");
        List<DietType> dietTypes = dietTypeMapper.selectList(new QueryWrapper<DietType>().orderByAsc("id"));
        List<ExportDietInfoVo> exportDietInfoVos = new ArrayList<>();
        List<DietInfo> dietInfos = dietInfoMapper.selectList(query);
        for (DietInfo dietInfo : dietInfos) {
            ExportDietInfoVo exportDietInfoVo = new ExportDietInfoVo();
            BeanUtils.copyProperties(dietInfo,exportDietInfoVo);
            BeanUtils.copyProperties(dietTypes.get(dietInfo.getType()-1),exportDietInfoVo);
            exportDietInfoVos.add(exportDietInfoVo);
        }
        return exportDietInfoVos;
    }
}
