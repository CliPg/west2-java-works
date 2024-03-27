package com.clipg.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.clipg.dto.ResponseResult;
import com.clipg.entity.User;
/**
 * @author 77507
 */
public interface UserService extends IService<User> {

    ResponseResult login(User user);
    ResponseResult register(User user);
    ResponseResult userInfo(String id);
    ResponseResult avatarUpload(String avatarUrl) throws Exception;
    boolean getUserByUsername(String username);

}
