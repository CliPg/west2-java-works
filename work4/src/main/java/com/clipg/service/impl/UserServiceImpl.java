package com.clipg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clipg.dao.UserDao;
import com.clipg.domain.LoginUser;
import com.clipg.domain.ResponseResult;
import com.clipg.domain.User;
import com.clipg.service.UserService;
import com.clipg.util.JwtUtil;
import com.clipg.util.RedisCache;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

@Service
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private UserDao userDao;

    /**
     * 根据post请求参数进行登录
     * @param user
     * @return
     */
    @Override
    public ResponseResult login(User user) {

        HashMap<String, String> map = null;
        try {
            // AuthenticationManager的authenticate方法进行用户认证
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
            Authentication authenticate = authenticationManager.authenticate(authenticationToken);
            // 如果认证没通过，给出对应提示
            if (Objects.isNull(authenticate)) {
                throw new RuntimeException("认证失败！");
            }
            //// 如果认证通过了，使用UUID(用户ID)生成JWT
            LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
            String userid = loginUser.getUser().getId().toString();
            //// 把完整的用户信息存入redis
            //String uuid = JwtUtil.getUUID();
            //String jwt = JwtUtil.createJWT(uuid);
            String jwt = JwtUtil.createJWT(userid);
            //loginUser.setUuid(uuid);
            redisCache.setCacheObject("login:" + userid,loginUser);
            //redisCache.setCacheObject("login_" + uuid, JSON.toJSONString(loginUser));
            //// 把token响应给前端
            map = new HashMap<>();
            map.put("token", jwt);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new ResponseResult(-1, "登录失败！");
        }

        return  new ResponseResult(200, "登录成功！", map);


    }

    /**
     * 根据post请求参数进行注册
     * @param registierUser
     * @return
     */
    @Override
    public ResponseResult register(User registierUser) {

        try {
            User user = new User();
            Date date = new Date();
            //保证用户名唯一
            if (getUserByUsername(registierUser.getUsername())){
                return new ResponseResult(-1, "用户名已被注册！");
            }
            //用户信息
            user.setUsername(registierUser.getUsername());
            user.setPassword(registierUser.getPassword());
            user.setCreateTime(date);
            userDao.insert(user);
            String id = user.getId().toString();
            redisCache.setCacheObject("register:" + id, user);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseResult(-1, "注册失败！");
        }

        return new ResponseResult(200, "注册成功！");

    }

    /**
     *查询用户信息
     * @param id
     * @return
     */
    @Override
    public ResponseResult info(String id) {

        String data = null;
        try {
            //根据id
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id", id);
            User user = userDao.selectOne(queryWrapper);
            if (user != null){
                data = user.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseResult(-1, "查询失败！");
        } finally {
            return  new ResponseResult(200, "查询成功！", data);
        }

    }

    /**
     * 上传头像
     * @param token
     * @param avatarUrl
     * @return
     */
    @Override
    public ResponseResult avatarUpload(String token, String avatarUrl) {

        try {
            //从token中获取用户id
            String id;
            Claims claims = JwtUtil.parseJWT(token);
            id = claims.getSubject();
            System.out.println(id);
            //上传头像
            UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id", id);
            User user = new User();
            Date date = new Date();
            user.setAvatarUrl(avatarUrl);
            user.setUpdateTime(date);
            userDao.update(user,updateWrapper);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseResult(-1, "上传失败！");
        }

        return  new ResponseResult(200, "上传成功！");



    }

    /**
     * 判断数据库是否有同名用户，保证用户名唯一
     * @param username
     * @return
     */
    @Override
    public boolean getUserByUsername(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        User user = userDao.selectOne(queryWrapper);
        return user != null;
    }
}
