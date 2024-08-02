package com.lzf.bibackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lzf.bibackend.model.entity.Chart;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ChartMapper extends BaseMapper<Chart> {
}