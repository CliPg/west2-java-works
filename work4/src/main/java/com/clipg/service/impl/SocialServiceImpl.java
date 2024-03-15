package com.clipg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clipg.dao.FollowDao;
import com.clipg.dao.UserDao;
import com.clipg.domain.Follow;
import com.clipg.domain.ResponseResult;
import com.clipg.domain.User;
import com.clipg.domain.Video;
import com.clipg.service.SocialService;
import com.clipg.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SocialServiceImpl implements SocialService {

    @Autowired
    private FollowDao followDao;

    @Autowired
    private UserDao userDao;


    @Override
    public ResponseResult follow(String token, String followingId, int actionType) {

        boolean flag = false;

        try {
            Claims claims = JwtUtil.parseJWT(token);
            String followerId = claims.getSubject();

            LambdaQueryWrapper<Follow> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(Follow::getFollowerId,followerId).eq(Follow::getFollowingId,followingId);
            Follow isFollow = followDao.selectOne(lambdaQueryWrapper);
            if (actionType == 0 && isFollow == null){
                Follow follow = new Follow();
                follow.setFollowerId(followerId);
                follow.setFollowingId(followingId);
                followDao.insert(follow);
                flag = true;
            } else if (actionType == 1 && isFollow != null) {
                QueryWrapper queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("following_id", followingId);
                followDao.delete(queryWrapper);
                flag = true;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            return flag ? new ResponseResult(200, "操作成功！") : new ResponseResult(-1, "操作失败！");
        }

    }

    @Override
    public ResponseResult followingList(String userId, int pageNum, int pageSize) {
        boolean flag = false;
        String data = null;
        try {
            Page<User> page = new Page<>(pageNum, pageSize);
            LambdaQueryWrapper<Follow> lqwFollow = new LambdaQueryWrapper<>();
            lqwFollow.eq(Follow::getFollowerId, userId);
            List<Follow> followList = followDao.selectList(lqwFollow);
            List<User> userList = new ArrayList<>();
            for (Follow follow : followList){
                String followingId = follow.getFollowingId();
                LambdaQueryWrapper<User> lqwUser = new LambdaQueryWrapper<>();
                lqwUser.select(User::getId,User::getUsername, User::getAvatarUrl).eq(User::getId, followingId);
                User user = userDao.selectOne(lqwUser);
                userList.add(user);
            }

            page.setRecords(userList);
            int total = userList.size(); // 获取总数
            data = "items:" + userList + ", total:" + total;
            if (total == 0) {
                data = "该用户还未关注其他用户！";
            }
            flag = true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            return flag ? new ResponseResult(200, "查询成功！", data) : new ResponseResult(-1, "查询失败！");
        }
    }

    @Override
    public ResponseResult followerList(String userId, int pageNum, int pageSize) {
        try {
            // 创建分页对象
            Page<User> page = new Page<>(pageNum, pageSize);

            // 构造查询条件：查找关注者是指定用户的关注记录
            LambdaQueryWrapper<Follow> lqwFollow = new LambdaQueryWrapper<>();
            lqwFollow.eq(Follow::getFollowingId, userId);

            // 根据查询条件查询关注记录列表
            List<Follow> followList = followDao.selectList(lqwFollow);

            // 从关注记录中获取关注者的用户ID
            List<String> followerIds = followList.stream().map(Follow::getFollowerId).collect(Collectors.toList());

            // 查询关注者的用户信息
            List<User> followerList = new ArrayList<>();
            if (!followerIds.isEmpty()) {
                LambdaQueryWrapper<User> lqwUser = new LambdaQueryWrapper<>();
                lqwUser.in(User::getId, followerIds);
                followerList = userDao.selectList(lqwUser);
            }

            // 将关注者列表设置到分页对象中
            page.setRecords(followerList);

            if (followerList.isEmpty()) {
                return new ResponseResult(-1, "该用户还没有粉丝！", null);
            } else {
                return new ResponseResult(200, "查询成功！", page);
            }
        } catch (Exception e) {
            throw new RuntimeException("查询粉丝列表失败！", e);
        }
    }

    @Override
    public ResponseResult friendsList(String token, int pageNum, int pageSize) {
        try {
            Claims claims = JwtUtil.parseJWT(token);
            String userId = claims.getSubject();

            // 创建分页对象
            Page<User> page = new Page<>(pageNum, pageSize);

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
            List<User> friendList = new ArrayList<>();
            if (!friendIds.isEmpty()) {
                LambdaQueryWrapper<User> lqwUser = new LambdaQueryWrapper<>();
                lqwUser.in(User::getId, friendIds);
                friendList = userDao.selectList(lqwUser);
            }

            // 将好友列表设置到分页对象中
            page.setRecords(friendList);

            if (friendList.isEmpty()) {
                return new ResponseResult(-1, "当前用户还没有好友！", null);
            } else {
                return new ResponseResult(200, "查询成功！", page);
            }
        } catch (Exception e) {
            throw new RuntimeException("查询好友列表失败！", e);
        }
    }

}
