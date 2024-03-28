package com.clipg.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.clipg.dto.ResponseResult;
import com.clipg.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author 77507
 */
public interface UserService extends IService<User> {

    ResponseResult login(User user);
    ResponseResult register(User user);
    ResponseResult userInfo(String id);
    ResponseResult avatarUpload(MultipartFile file) throws IOException;
    boolean getUserByUsername(String username);

}
