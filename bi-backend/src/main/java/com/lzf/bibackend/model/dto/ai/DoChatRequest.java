package com.lzf.bibackend.model.dto.ai;

import lombok.Data;

import java.io.Serializable;

@Data
public class DoChatRequest implements Serializable {
    private static final long serialVersionUID = 2407070867474817692L;
    private String name;
    private String question;
    private String data;
    private String chatType;
}
