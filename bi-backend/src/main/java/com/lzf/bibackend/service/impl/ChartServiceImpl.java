package com.lzf.bibackend.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzf.bibackend.model.entity.Chart;
import com.lzf.bibackend.mapper.ChartMapper;
import com.lzf.bibackend.service.ChartService;
@Service
public class ChartServiceImpl extends ServiceImpl<ChartMapper, Chart> implements ChartService{

}
