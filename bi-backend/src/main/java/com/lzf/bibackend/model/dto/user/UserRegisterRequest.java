package com.lzf.bibackend.model.dto.user;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = 66830988886263800L;

    private String userAccount;

    private String userPassword;

    private String checkPassword;
}
