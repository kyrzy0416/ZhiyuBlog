package com.zhiyu.dao;

import com.zhiyu.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface UserDao extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    User findByUsernameAndPassword(String username, String password);

    User findByUsername(String username);
}
