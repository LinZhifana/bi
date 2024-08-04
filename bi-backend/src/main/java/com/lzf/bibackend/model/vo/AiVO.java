package com.lzf.bibackend.model.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class AiVO implements Serializable {
    private static final long serialVersionUID = 2962710936515935873L;
    /**
     * chart id
     */
    private Long id;
    /**
     * echarts option
     */
    private String option;
    /**
     * 结论
     */
    private String message;
}
