package com.zhiyu.service;

import com.zhiyu.domain.User;

public interface UserService {

    User checkUser(String username, String password);
}
