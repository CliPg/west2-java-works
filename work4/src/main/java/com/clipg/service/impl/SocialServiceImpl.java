package com.clipg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clipg.dto.*;
import com.clipg.exception.BusinessException;
import com.clipg.mapper.FollowMapper;
import com.clipg.mapper.UserMapper;
import com.clipg.entity.*;
import com.clipg.service.SocialService;
import com.clipg.util.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 77507
 */
@Service
public class SocialServiceImpl implements SocialService {
    @Autowired
    private FollowMapper followMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserHolder userHolder;

    /**
     * 关注/取关用户
     */
    @Transactional
    @Override
    public ResponseResult follow(String followingId, int actionType) throws Exception {
        String followerId = userHolder.getUserId();
        // 检查是否自己关注自己
        if (followerId.equals(followingId)) {
            throw new BusinessException(Code.ERROR, Message.ERROR);
        }
        LambdaQueryWrapper<Follow> lqw = new LambdaQueryWrapper<>();
        Follow isFollow = followMapper.selectOne(lqw.eq(Follow::getFollowerId,followerId).eq(Follow::getFollowingId,followingId));
        if (actionType == 0 && isFollow == null){
            followMapper.insert(new Follow(followerId,followingId));
        } else if (actionType == 1 && isFollow != null) {
            QueryWrapper qw = new QueryWrapper<>();
            qw.eq("following_id", followingId);
            followMapper.delete(qw);
        } else {
            throw new BusinessException(Code.ERROR,Message.ERROR);
        }
        return new ResponseResult(Code.SUCCESS, Message.SUCCESS);
    }


    /**
     * 查询指定用户的关注名单
     */
    @Override
    public ResponseResult followingList(String userId, int pageNum, int pageSize) {
        LambdaQueryWrapper<Follow> lqwFollow = new LambdaQueryWrapper<>();
        List<Follow> followList = followMapper.selectList(lqwFollow.eq(Follow::getFollowerId, userId));
        List<UserDto> userList = new ArrayList<>();
        for (Follow follow : followList){
            LambdaQueryWrapper<User> lqwUser = new LambdaQueryWrapper<>();
            User user = userMapper.selectOne(lqwUser.select(User::getId,User::getUsername, User::getAvatarUrl).eq(User::getId, follow.getFollowingId()));
            UserDto userDto = new UserDto();
            userDto.setId(user.getId());
            userDto.setId(user.getUsername());
            userDto.setId(user.getAvatarUrl());
            userList.add(userDto);
        }
        if (userList.isEmpty()) {
            return new ResponseResult(Code.ERROR,Message.ERROR);
        }
        Page<UserDto> page = new Page<>(pageNum, pageSize);
        page.setRecords(userList);
        return new ResponseResult(Code.SUCCESS, Message.SUCCESS, new FollowDto(page.getRecords(),userList.size()));
    }


    /**
     * 查询指定用户的粉丝名单
     */
    @Override
    public ResponseResult followerList(String userId, int pageNum, int pageSize) {
        //根据userId查找在数据库中的指定行，并获得相应列表
        LambdaQueryWrapper<Follow> lqwFollow = new LambdaQueryWrapper<>();
        List<Follow> followList = followMapper.selectList(lqwFollow.eq(Follow::getFollowingId, userId));
        List<UserDto> userList = new ArrayList<>();
        for (Follow follow : followList) {
            String followerId = follow.getFollowerId();
            LambdaQueryWrapper<User> lqwUser = new LambdaQueryWrapper<>();
            lqwUser.select(User::getId, User::getUsername, User::getAvatarUrl).eq(User::getId, followerId);
            User user = userMapper.selectOne(lqwUser);
            UserDto userDto = new UserDto();
            userDto.setId(user.getId());
            userDto.setId(user.getUsername());
            userDto.setId(user.getAvatarUrl());
            userList.add(userDto);
        }
        if (userList.isEmpty()) {
            return new ResponseResult(Code.ERROR,Message.ERROR);
        }
        Page<UserDto> page = new Page<>(pageNum, pageSize);
        page.setRecords(userList);
        return new ResponseResult(Code.SUCCESS, Message.SUCCESS, new FollowDto(page.getRecords(),userList.size()));

    }


    /**
     * 获取好友列表
     */
    @Override
    public ResponseResult friendsList(int pageNum, int pageSize){
        String userId = userHolder.getUserId();
        // 提取好友的用户ID
        List<String> friendIds = getFriendsIds(userId);
        // 查询好友的用户信息
        List<UserDto> friendList = new ArrayList<>();
        if (!friendIds.isEmpty()) {
            LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
            List<User> userList = userMapper.selectList(lqw.in(User::getId, friendIds));
            for (User user : userList) {
                UserDto userDto = new UserDto();
                userDto.setId(user.getId());
                userDto.setId(user.getUsername());
                userDto.setId(user.getAvatarUrl());
                friendList.add(userDto);
            }
        }
        if (friendList.isEmpty()) {
            return new ResponseResult(Code.ERROR,Message.ERROR);
        }
        int total = friendList.size();
        if (pageNum > total || pageSize > total || pageNum < 0 || pageSize <= 0){
            throw  new BusinessException(Code.ERROR,Message.ERROR);
        }
        Page<UserDto> page = new Page<>(pageNum, pageSize);
        page.setRecords(friendList);
        return new ResponseResult(Code.SUCCESS, Message.SUCCESS, new FollowDto(page.getRecords(),total));

    }


    /**
     * 获取好友的id
     */
    public List<String> getFriendsIds(String userId){
        // 构造查询条件：查找用户A关注了用户B且用户B关注了用户A的记录
        LambdaQueryWrapper<Follow> lqwFollow = new LambdaQueryWrapper<>();
        lqwFollow.eq(Follow::getFollowerId, userId);
        // 查询符合条件的关注记录列表
        List<Follow> followList = followMapper.selectList(lqwFollow);
        // 提取好友的用户ID
        List<String> friendIds = new ArrayList<>();
        for (Follow follow : followList) {
            String followingId = follow.getFollowingId();
            // 检查用户B是否也关注了用户A
            LambdaQueryWrapper<Follow> lqwReverseFollow = new LambdaQueryWrapper<>();
            int count = followMapper.selectCount(lqwReverseFollow.eq(Follow::getFollowerId, followingId)
                    .eq(Follow::getFollowingId, userId));
            if (count > 0) {
                friendIds.add(followingId);
            }
        }
        return friendIds;
    }

}
