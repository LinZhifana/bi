package com.lzf.bibackend.model.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class AiVO implements Serializable {
    private static final long serialVersionUID = 2962710936515935873L;
    private Long id;
    private String option;
    private String message;
}
