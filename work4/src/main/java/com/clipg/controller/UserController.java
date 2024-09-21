package com.clipg.controller;

import com.clipg.dto.ResponseResult;
import com.clipg.entity.User;
import com.clipg.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author 77507
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseResult login(@RequestBody User user){
        return userService.login(user);
    }

    @PostMapping("/register")
    public ResponseResult register(@RequestBody User user){
        return userService.register(user);
    }

    @GetMapping("/info")
    public ResponseResult info(@RequestParam("id") String id){
        return userService.userInfo(id);
    }

    @PostMapping("/avatar/upload")
    public ResponseResult avatarUpload(@RequestParam("data")MultipartFile file) throws IOException {
        return userService.avatarUpload(file);
    }
}
