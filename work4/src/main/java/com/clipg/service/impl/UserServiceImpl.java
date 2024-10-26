package com.clipg.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clipg.dto.Code;
import com.clipg.dto.LoginDto;
import com.clipg.dto.Message;
import com.clipg.dto.UserDto;
import com.clipg.exception.BusinessException;
import com.clipg.filter.LogFilter;
import com.clipg.mapper.UserMapper;
import com.clipg.entity.LoginUser;
import com.clipg.dto.ResponseResult;
import com.clipg.entity.User;
import com.clipg.service.UserService;
import com.clipg.util.JwtUtil;
import com.clipg.util.RedisCache;
import com.clipg.util.UserHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

/**
 * @author 77507
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserHolder userHolder;

    private static final Logger LOG = LoggerFactory.getLogger(LogFilter.class);

    /**
     * 根据post请求参数进行登录
     */
    @Override
    public ResponseResult login(User dto) {
        // AuthenticationManager的authenticate方法进行用户认证
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        LOG.info("正在登录");
        // 如果认证没通过，给出对应提示
        if (Objects.isNull(authenticate)) {
            LOG.info("认证不通过");
            return new ResponseResult(Code.ERROR, Message.ERROR);
        }
        // 如果认证通过了，使用UUID(用户ID)生成JWT
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userid = loginUser.getUser().getId();
        String token = JwtUtil.createJwt(userid);
        //存入redis
        redisCache.setCacheObject("login:" + userid,loginUser);
        return new ResponseResult(Code.SUCCESS, Message.SUCCESS, new LoginDto(token));
    }

    @Override
    public ResponseResult logout() {

        // 获取当前登录用户的认证信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();

        // 从登录用户中获取用户ID
        String userId = loginUser.getUser().getId();

        // 从 Redis 中删除对应的登录信息
        redisCache.deleteObject("login:" + userId);

        // 清除 Spring Security 上下文中的用户信息
        SecurityContextHolder.clearContext();

        // 返回退出登录成功的结果
        return new ResponseResult(Code.SUCCESS, Message.SUCCESS);
    }


    /**
     * 根据post请求参数进行注册
     */
    @Transactional
    @Override
    public ResponseResult register(User dto) {
        //保证用户名唯一
        if (getUserByUsername(dto.getUsername())){
            return new ResponseResult(Code.ERROR,Message.ERROR);
        }
        //用户信息
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setCreateTime(new Date());
        userMapper.insert(user);
        //存入redis 目前感觉没什么必要
        //redisCache.setCacheObject("register:" + user.getId(), user);
        return new ResponseResult(Code.SUCCESS, Message.SUCCESS);

    }

    /**
     *查询用户信息
     */
    @Override
    public ResponseResult userInfo(String id) {
        //根据id查找指定用户
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        User user = userMapper.selectOne(queryWrapper);
        //判断user是否存在
        if (user == null){
            throw new BusinessException(Code.ERROR,Message.ERROR);
        }
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setAvatarUrl(user.getAvatarUrl());
        userDto.setCreateTime(user.getCreateTime());
        userDto.setCreateTime(user.getUpdateTime());
        userDto.setCreateTime(user.getDeleteTime());

        return  new ResponseResult(Code.SUCCESS, Message.SUCCESS, userDto);
    }

    /**
     * 上传头像
     */
    @Transactional
    @Override
    public ResponseResult avatarUpload(MultipartFile data) throws IOException {
        String id = userHolder.getUserId();
        // 获取上传文件的原始文件名
        String originalFilename = data.getOriginalFilename();
        System.out.println(originalFilename);
        // 获取文件后缀
        String fileExtension = getFileExtension(originalFilename);
        // 仅保留图片文件
        if (!Arrays.asList("jpg", "png", "gif").contains(fileExtension.toLowerCase())) {
            throw new BusinessException(Code.ERROR,Message.ERROR);
        }

        String fileName = UUID.randomUUID() + "." + fileExtension;

        String endpoint = "";
        String accessKeyId = "";
        String accessKeySecret = "";
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        InputStream inputStream = data.getInputStream();
        ossClient.putObject("clipg-work4-videos", fileName, inputStream);
        ossClient.shutdown();

        //上传头像
        String avatarUrl = "http://localhost:8080/avatar/upload" + fileName;
        User user = userMapper.selectById(id);
        if (user == null){
            throw new BusinessException(Code.ERROR,Message.ERROR);
        }
        user.setAvatarUrl(avatarUrl);
        user.setUpdateTime(new Date());
        return new ResponseResult(Code.SUCCESS,Message.SUCCESS);
    }

    public String getFileExtension(String originalFilename){
        String fileExtension = "";
        if (originalFilename != null && !originalFilename.isEmpty()) {
            int lastDotIndex = originalFilename.lastIndexOf(".");
            if (lastDotIndex != -1) {
                fileExtension = originalFilename.substring(lastDotIndex + 1);
            }else {
                throw new BusinessException(Code.ERROR, Message.ERROR);
            }
        }
        return fileExtension;
    }

    /**
     * 判断数据库是否有同名用户，保证用户名唯一
     */
    public boolean getUserByUsername(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        User user = userMapper.selectOne(queryWrapper);
        return user != null;
    }
}
