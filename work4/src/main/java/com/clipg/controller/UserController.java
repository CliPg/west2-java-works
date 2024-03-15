package com.clipg.controller;

import com.clipg.domain.ResponseResult;
import com.clipg.domain.User;
import com.clipg.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/login")
    public ResponseResult login(@RequestBody User user){
        return userService.login(user);
    }

    @RequestMapping("/register")
    public ResponseResult register(@RequestBody User user){
        return userService.register(user);
    }

    @GetMapping("/info")
    public ResponseResult info(@RequestParam("id") String id){
        return userService.info(id);
    }

    @PutMapping("/avatar/upload")
    public ResponseResult avatarUpload(@RequestParam("avatarUrl") String avatarUrl,
                                       @RequestHeader("token") String token){
        return userService.avatarUpload(token, avatarUrl);
    }
}
