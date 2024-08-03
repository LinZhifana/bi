package com.lzf.bibackend.model.dto.user;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserQueryRequest implements Serializable {
    private static final long serialVersionUID = -5241182183791231831L;
    private Long id;
    private String userAccount;
}
