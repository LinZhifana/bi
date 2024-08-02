package com.lzf.bibackend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lzf.bibackend.model.dto.user.UserQueryRequest;
import com.lzf.bibackend.model.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lzf.bibackend.model.vo.UserVO;
import jakarta.servlet.http.HttpServletRequest;

public interface UserService extends IService<User> {

    public long userRegister(String userAccount, String userPassword, String checkPassword);
    public UserVO userLogin(String userAccount, String userPassword, HttpServletRequest request);
    public boolean userLogout(HttpServletRequest request);
    public User getLoginUser(HttpServletRequest request);
    public QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest);
}

