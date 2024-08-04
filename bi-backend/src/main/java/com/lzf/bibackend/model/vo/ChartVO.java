package com.lzf.bibackend.model.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class ChartVO implements Serializable {

    private static final long serialVersionUID = -5220541064839639595L;

    /**
     * chart id
     */
    private Long id;

    /**
     * 图表类型
     */
    private String chartType;

    /**
     * 图表名称
     */
    private String name;

    /**
     * 分析目标
     */
    private String goal;

    /**
     * 生成的图表数据
     */
    private String genChart;

    /**
     * 生成的分析结论
     */
    private String genResult;
}
