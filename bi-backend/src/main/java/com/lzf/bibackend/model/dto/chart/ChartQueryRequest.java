package com.lzf.bibackend.model.dto.chart;

import lombok.Data;

import java.io.Serializable;
@Data
public class ChartQueryRequest implements Serializable {
    private static final long serialVersionUID = -7424775512882030521L;
    /**
     * Chart id
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 分析目标
     */
    private String goal;

    /**
     * 图表类型
     */
    private String chartType;

    /**
     * 用户 id
     */
    private Long userId;

    /**
     * 页号
     */
    private int pageNum;

    /**
     * 页面数量
     */
    private int pageSize;
}
