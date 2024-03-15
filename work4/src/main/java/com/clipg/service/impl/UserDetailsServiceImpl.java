package com.clipg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.clipg.dao.UserDao;
import com.clipg.domain.LoginUser;
import com.clipg.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        User user = userDao.selectOne(queryWrapper);

        if (user == null){
            throw new UsernameNotFoundException(username);
        }else {

            return new LoginUser(user);

        }
    }
}
