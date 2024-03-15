package com.clipg.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.clipg.domain.ResponseResult;
import com.clipg.domain.User;

public interface UserService extends IService<User> {

    ResponseResult login(User user);

    ResponseResult register(User user);

    ResponseResult info(String id);

    ResponseResult avatarUpload(String token, String avatarUrl);
    boolean getUserByUsername(String username);

}
