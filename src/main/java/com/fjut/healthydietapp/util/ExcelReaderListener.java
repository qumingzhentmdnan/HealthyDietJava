package com.fjut.healthydietapp.util;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.exception.ExcelDataConvertException;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fjut.healthydietapp.config.CustomizedException;
import com.fjut.healthydietapp.entity.DietInfo;
import com.fjut.healthydietapp.entity.DietType;
import com.fjut.healthydietapp.mapper.DietInfoMapper;
import com.fjut.healthydietapp.mapper.DietTypeMapper;
import com.fjut.healthydietapp.service.DietInfoService;
import com.fjut.healthydietapp.vo.ExportDietInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.util.List;

// 有个很重要的点 ExportDietInfoVoListener 不能被spring管理，要每次读取excel都要new,然后里面用到spring可以构造方法传进去
@Slf4j
public class ExcelReaderListener implements ReadListener<ExportDietInfoVo> {

    private DietTypeMapper dietTypeMapper;

    private DietInfoService dietInfoService;

    private Integer id;

    public ExcelReaderListener(DietTypeMapper dietTypeMapper, DietInfoService dietInfoService, Integer id) {
        this.dietTypeMapper = dietTypeMapper;
        this.dietInfoService = dietInfoService;
        this.id = id;
    }

    private List<DietInfo> list = ListUtils.newArrayList();

    @Override
    public void invoke(ExportDietInfoVo exportDietInfoVo, AnalysisContext analysisContext) {
            DietInfo dietInfo = new DietInfo();
            BeanUtils.copyProperties(exportDietInfoVo, dietInfo);
            QueryWrapper<DietType> query = new QueryWrapper<DietType>().eq("type_name", exportDietInfoVo.getTypeName());
            dietInfo.setType(dietTypeMapper.selectOne(query).getId());
            dietInfo.setUserId(id);
            if(dietInfo.getFoodName().length()>50){
                throw new CustomizedException(20001, dietInfo.getFoodName() + "名称过长");
            }
            if (list.size() >= 50) {
                dietInfoService.saveBatch(list, list.size());
                list.clear();
            } else {
                list.add(dietInfo);
            }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        if(!list.isEmpty())
            dietInfoService.saveBatch(list,list.size());
    }

    @Override
    public void onException(Exception exception, AnalysisContext context) throws Exception {
        log.error("解析失败", exception.getMessage());
        // 如果是某一个单元格的转换异常 能获取到具体行号
        // 如果要获取头的信息 配合invokeHeadMap使用
        if (exception instanceof ExcelDataConvertException) {
            ExcelDataConvertException excelDataConvertException = (ExcelDataConvertException)exception;
            StringBuilder err = new StringBuilder();
            String res = err.append("第").append(excelDataConvertException.getRowIndex()+1).append("行，第")
                    .append(excelDataConvertException.getColumnIndex()+1).append("列解析异常，数据为:")
                    .append(excelDataConvertException.getCellData()).toString();
            throw new CustomizedException(20001,res);
        }else if(exception instanceof CustomizedException){
            throw exception;
        }
    }
}