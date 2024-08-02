package com.lzf.bibackend.model.dto.user;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = -830744162766450694L;

    private String userAccount;

    private String userPassword;
}
