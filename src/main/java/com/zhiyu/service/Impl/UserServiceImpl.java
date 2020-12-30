package com.zhiyu.service.Impl;

import com.zhiyu.dao.UserDao;
import com.zhiyu.domain.User;
import com.zhiyu.service.UserService;
import com.zhiyu.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public User checkUser(String username, String password) {
        User user = userDao.findByUsernameAndPassword(username, MD5Utils.code(password));
        return user;
    }
}
