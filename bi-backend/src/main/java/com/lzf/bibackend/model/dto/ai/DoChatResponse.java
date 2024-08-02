package com.lzf.bibackend.model.dto.ai;

import lombok.Data;

@Data
public class DoChatResponse {
    private Long chartId;
    private String option;
    private String message;
}
