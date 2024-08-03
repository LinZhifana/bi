package com.lzf.bibackend.model.dto.ai;

import lombok.Data;

import java.io.Serializable;

@Data
public class DoChatResponse implements Serializable {
    private static final long serialVersionUID = 2567922548432432433L;
    private Long chartId;
    private String option;
    private String message;
}
