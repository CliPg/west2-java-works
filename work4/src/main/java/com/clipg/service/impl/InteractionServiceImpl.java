package com.clipg.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clipg.dto.*;
import com.clipg.entity.*;
import com.clipg.exception.BusinessException;
import com.clipg.mapper.CommentMapper;
import com.clipg.mapper.LikeMapper;
import com.clipg.mapper.VideoMapper;
import com.clipg.service.InteractionService;
import com.clipg.service.VideoService;
import com.clipg.util.UserHolder;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author 77507
 */
@Service
public class InteractionServiceImpl implements InteractionService {

    @Autowired
    private VideoService videoService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private LikeMapper likeMapper;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private VideoMapper videoMapper;
    @Autowired
    private UserHolder userHolder;

    /**
     * 对视频进行点赞/取赞
     */
    @Transactional
    @Override
    public ResponseResult likeVideo(String actionType, String videoId) {
        String userId = userHolder.getUserId();
        //判断当前用户是否已经点赞
        String key = "videoId:" + videoId + ":likes";
        boolean isMember = Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(key, userId));
        if (BooleanUtils.isFalse(isMember) && "1".equals(actionType)){
            boolean isSuccess = videoService.update().setSql("like_count = like_count + 1").eq("id", videoId).update();
            if (isSuccess){
                //存入redis
                redisTemplate.opsForSet().add(key, userId);
                //存入数据库
                VideoLikes videoLikes = new VideoLikes();
                videoLikes.setUserId(userId);
                videoLikes.setVideoId(videoId);
                likeMapper.insert(videoLikes);
            }else {
                return new ResponseResult(Code.ERROR, Message.ERROR);
            }
        }else if (!BooleanUtils.isFalse(isMember) && "2".equals(actionType)) {
            boolean isSuccess = videoService.update().setSql("like_count = like_count - 1").eq("id", videoId).update();
            if (isSuccess){
                //移除redis
                redisTemplate.opsForSet().remove(key, userId);
                //移除数据库
                LambdaQueryWrapper<VideoLikes> lqw = new LambdaQueryWrapper<>();
                lqw.eq(VideoLikes::getUserId, userId).eq(VideoLikes::getVideoId, videoId);
                likeMapper.delete(lqw);
            }else {
                return new ResponseResult(Code.ERROR, Message.ERROR);
            }
        }else {
            throw new BusinessException(Code.ERROR, Message.ERROR);
        }
        return new ResponseResult(Code.SUCCESS, Message.SUCCESS);
    }

    /**
     * 返回指定用户点赞的视频
     */
    @Override
    public ResponseResult listVideoLikeByUserId(String userId) {
        LambdaQueryWrapper<VideoLikes> lqw = new LambdaQueryWrapper<>();
        lqw.eq(VideoLikes::getUserId,userId);
        List<VideoLikes> videoLikesList = likeMapper.selectList(lqw);
        List<String> videoIdList = new ArrayList<>();
        for (VideoLikes videoLikes : videoLikesList) {
            videoIdList.add(videoLikes.getVideoId());
        }
        LambdaQueryWrapper<Video> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Video::getId, videoIdList);
        List<Video> videoList = videoMapper.selectList(queryWrapper);
        if (videoList.isEmpty()){
            return new ResponseResult(Code.ERROR,Message.ERROR);
        }
        return new ResponseResult(Code.SUCCESS, Message.SUCCESS, new VideoDto(videoList,videoList.size()));
    }

    /**
     * 对视频评论
     */
    @Transactional
    @Override
    public ResponseResult publishCommentToVideo(String videoId, String content)  {
        String userId = userHolder.getUserId();
        Comment comment = new Comment();
        comment.setUserId(userId);
        comment.setVideoId(videoId);
        comment.setContent(content);
        comment.setCreateTime(new Date());
        commentMapper.insert(comment);
        return new ResponseResult(Code.SUCCESS, Message.SUCCESS);
    }

    /**
     * 对评论评论
     */
    @Transactional
    @Override
    public ResponseResult publishCommentToComment(String parentId, String content) {
        String userId = userHolder.getUserId();
        Comment commentParent = commentMapper.selectById(parentId);
        Comment comment = new Comment();
        comment.setUserId(userId);
        comment.setVideoId(commentParent.getVideoId());
        comment.setParentId(parentId);
        comment.setContent(content);
        comment.setCreateTime(new Date());
        commentParent.setChildCount(commentParent.getChildCount()+1);
        commentMapper.insert(comment);
        commentMapper.updateById(commentParent);
        return new ResponseResult(Code.SUCCESS, Message.SUCCESS);
    }

    /**
     * 获取视频的评论列表
     */
    @Override
    public ResponseResult commentList(String videoId, int pageNum, int pageSize) {
        QueryWrapper<Comment> qw = new QueryWrapper<>();
        qw.eq("video_id", videoId);
        Page<Comment> page = new Page<>(pageNum, pageSize);
        IPage<Comment> commentPage = commentMapper.selectPage(page, qw);
        List<Comment> commentList = commentPage.getRecords();
        if (commentList.isEmpty()) {
            return new ResponseResult(Code.ERROR, Message.ERROR);
        }
        int total = (int) commentPage.getTotal();
        return new ResponseResult(Code.SUCCESS, Message.SUCCESS, new CommentDto(page.getRecords(), total));
    }


    /**
     * 用户删除自己已发表的评论
     */
    @Transactional
    @Override
    public ResponseResult commentDelete(String commentId){
        String userId = userHolder.getUserId();
        Comment comment = commentMapper.selectById(commentId);
        if (comment == null){
            return new ResponseResult(Code.ERROR,Message.ERROR);
        }
        if (!comment.getUserId().equals(userId)){
            return new ResponseResult(Code.ERROR,Message.ERROR);
        }
        commentMapper.deleteById(commentId);
        videoService.update().setSql("comment_count = comment_count - 1").eq("id", comment.getVideoId()).update();
        if (comment.getParentId() != null){
            Comment commentParent = commentMapper.selectById(comment.getParentId());
            commentParent.setChildCount(commentParent.getChildCount()-1);
        }
        return new ResponseResult(Code.SUCCESS, Message.SUCCESS);
    }
}
