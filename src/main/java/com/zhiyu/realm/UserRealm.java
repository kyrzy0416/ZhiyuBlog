package com.zhiyu.realm;

import com.zhiyu.dao.UserDao;
import com.zhiyu.domain.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

public class UserRealm extends AuthorizingRealm {

    @Autowired
    UserDao userDao;

    /**
     * 授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        System.out.println("授权.............");
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        // 角色
        Set<String> roles = new HashSet<>();
        // 权限
//        Set<String> permissions = new HashSet<>();
        // 加入角色和权限
        /**
         * 目前没有改数据库只有一个用户，暂时不开放注册
         */
        if ("17716504161".equals(user.getUsername())) {
            roles.add("admin");
            info.setRoles(roles);
        }
        return info;
    }

    /**
     * 认证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("认证..............");
        String username = (String) token.getPrincipal();
        User user = userDao.findByUsername(username);
        if (user == null)
            throw new UnknownAccountException();
        // MD5盐值加密
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, user.getPassword(), ByteSource.Util.bytes(username), getName());
        return info;
    }
}
