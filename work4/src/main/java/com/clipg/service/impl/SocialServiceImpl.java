package com.clipg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clipg.dao.FollowDao;
import com.clipg.dao.UserDao;
import com.clipg.domain.*;
import com.clipg.service.SocialService;
import com.clipg.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SocialServiceImpl implements SocialService {

    @Autowired
    private FollowDao followDao;

    @Autowired
    private UserDao userDao;


    /**
     * 关注/取关用户
     * @param token
     * @param followingId
     * @param actionType
     * @return
     */
    @Override
    public ResponseResult follow(String token, String followingId, int actionType) {

        try {
            Claims claims = JwtUtil.parseJWT(token);
            String followerId = claims.getSubject();

            // 检查是否自己关注自己
            if (followerId.equals(followingId)) {
                return new ResponseResult(-1, "无法关注或取关自己！");
            }

            LambdaQueryWrapper<Follow> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(Follow::getFollowerId,followerId).eq(Follow::getFollowingId,followingId);
            Follow isFollow = followDao.selectOne(lambdaQueryWrapper);
            //点赞，且未关注
            if (actionType == 0 && isFollow == null){
                //保存数据库中
                Follow follow = new Follow();
                follow.setFollowerId(followerId);
                follow.setFollowingId(followingId);
                followDao.insert(follow);
            } else if (actionType == 1 && isFollow != null) {
                QueryWrapper queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("following_id", followingId);
                followDao.delete(queryWrapper);
            } else {
                return new ResponseResult(-1, "操作失败！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseResult(-1, "操作失败！");
        }

        return new ResponseResult(200, "操作成功！");
    }


    /**
     * 查询指定用户的关注名单
     * @param userId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public ResponseResult followingList(String userId, int pageNum, int pageSize) {

        Datas data = new Datas<>();
        try {
            Page<UserInfo> page = new Page<>(pageNum, pageSize);

            //根据userId查找在数据库中的指定行，并获得相应列表
            LambdaQueryWrapper<Follow> lqwFollow = new LambdaQueryWrapper<>();
            lqwFollow.eq(Follow::getFollowerId, userId);
            List<Follow> followList = followDao.selectList(lqwFollow);
            List<UserInfo> userList = new ArrayList<>();
            for (Follow follow : followList){
                String followingId = follow.getFollowingId();
                LambdaQueryWrapper<User> lqwUser = new LambdaQueryWrapper<>();
                lqwUser.select(User::getId,User::getUsername, User::getAvatarUrl).eq(User::getId, followingId);
                User user = userDao.selectOne(lqwUser);
                //转成UserInfo信息
                UserInfo userInfo = new UserInfo();
                userInfo.setId(user.getId());
                userInfo.setUsername(user.getUsername());
                userInfo.setAvatarUrl(user.getAvatarUrl());
                userList.add(userInfo);
            }

            page.setRecords(userList);
            int total = userList.size();
            data.setItems(userList);
            data.setTotal(total);
            if (total == 0) {
                return new ResponseResult(-1, "该用户还未关注其他用户！");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseResult(-1, "查询失败！");
        }

        return new ResponseResult(200, "查询成功！", data);

    }


    /**
     * 查询指定用户的粉丝名单
     * @param userId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public ResponseResult followerList(String userId, int pageNum, int pageSize) {

        Datas data = new Datas<>();
        try {
            Page<UserInfo> page = new Page<>(pageNum, pageSize);

            //根据userId查找在数据库中的指定行，并获得相应列表
            LambdaQueryWrapper<Follow> lqwFollow = new LambdaQueryWrapper<>();
            lqwFollow.eq(Follow::getFollowingId, userId);
            List<Follow> followList = followDao.selectList(lqwFollow);
            List<UserInfo> userList = new ArrayList<>();
            for (Follow follow : followList) {
                String followerId = follow.getFollowerId();
                LambdaQueryWrapper<User> lqwUser = new LambdaQueryWrapper<>();
                lqwUser.select(User::getId, User::getUsername, User::getAvatarUrl).eq(User::getId, followerId);
                User user = userDao.selectOne(lqwUser);
                //转成UserInfo信息
                UserInfo userInfo = new UserInfo();
                userInfo.setId(user.getId());
                userInfo.setUsername(user.getUsername());
                userInfo.setAvatarUrl(user.getAvatarUrl());
                userList.add(userInfo);
            }

            page.setRecords(userList);
            int total = userList.size(); // 获取总数
            data.setItems(userList);
            data.setTotal(total);
            if (total == 0) {
                return new ResponseResult(-1, "该用户还没有粉丝！");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseResult(-1, "查询失败！");
        }

        return new ResponseResult(200, "查询成功！", data);

    }


    @Override
    public ResponseResult friendsList(String token, int pageNum, int pageSize) {

        Datas data = new Datas<>();
        try {
            Claims claims = JwtUtil.parseJWT(token);
            String userId = claims.getSubject();

            // 构造查询条件：查找用户A关注了用户B且用户B关注了用户A的记录
            LambdaQueryWrapper<Follow> lqwFollow = new LambdaQueryWrapper<>();
            lqwFollow.eq(Follow::getFollowerId, userId);

            // 查询符合条件的关注记录列表
            List<Follow> followList = followDao.selectList(lqwFollow);

            // 提取好友的用户ID
            List<String> friendIds = new ArrayList<>();
            for (Follow follow : followList) {
                String followingId = follow.getFollowingId();
                // 检查用户B是否也关注了用户A
                LambdaQueryWrapper<Follow> lqwReverseFollow = new LambdaQueryWrapper<>();
                lqwReverseFollow.eq(Follow::getFollowerId, followingId)
                        .eq(Follow::getFollowingId, userId);
                int count = followDao.selectCount(lqwReverseFollow);
                if (count > 0) {
                    friendIds.add(followingId);
                }
            }

            // 查询好友的用户信息
            List<UserInfo> friendList = new ArrayList<>();
            if (!friendIds.isEmpty()) {
                LambdaQueryWrapper<User> lqwUser = new LambdaQueryWrapper<>();
                lqwUser.in(User::getId, friendIds);
                List<User> userList = userDao.selectList(lqwUser);

                // 构建用户信息列表
                for (User user : userList) {
                    UserInfo userInfo = new UserInfo();
                    userInfo.setId(user.getId());
                    userInfo.setUsername(user.getUsername());
                    userInfo.setAvatarUrl(user.getAvatarUrl());
                    friendList.add(userInfo);
                }
            }

            if (friendList.isEmpty()) {
                return new ResponseResult(-1, "当前用户还没有好友！");
            } else {
                data.setItems(friendList);
                data.setTotal(friendList.size());
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseResult(-1, "查询失败！");
        } finally {
            return new ResponseResult(200, "查询成功！", data);
        }
    }



}
