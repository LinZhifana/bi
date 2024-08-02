package com.lzf.bibackend.model.dto.user;

import lombok.Data;

@Data
public class UserQueryRequest {
    private Long id;
    private String userAccount;
}
