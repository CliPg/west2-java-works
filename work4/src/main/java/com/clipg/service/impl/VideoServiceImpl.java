package com.clipg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.clipg.dto.Code;
import com.clipg.dto.Message;
import com.clipg.dto.VideoDto;
import com.clipg.exception.BusinessException;
import com.clipg.mapper.VideoMapper;
import com.clipg.dto.ResponseResult;

import com.clipg.entity.Video;
import com.clipg.service.VideoService;


import com.clipg.task.UploadVideoTask;
import com.clipg.util.RedisCache;
import com.clipg.util.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.*;


/**
 * @author 77507
 */
@Slf4j
@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video> implements VideoService {

    @Autowired
    private VideoMapper videoMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private UserHolder userHolder;
    @Autowired
    private UploadVideoTask uploadVideoTask;

    /**
     * 上传视频
     * 异步处理
     */
    @Transactional
    @Override
    public ResponseResult publishVideo(MultipartFile data, String title, String description) throws Exception {
        String userId = userHolder.getUserId();
        // 获取上传文件的原始文件名
        String originalFilename = data.getOriginalFilename();
        // 获取文件后缀
        String fileExtension = getFileExtension(originalFilename);
        // 仅保留视频文件
        if (!Arrays.asList("mp4", "mov", "avi", "mkv", "wmv", "flv", "webm", "m4v", "3gp").contains(fileExtension.toLowerCase())) {
            throw new BusinessException(Code.ERROR,Message.ERROR);
        }
        // 生成视频文件名
        String fileName = UUID.randomUUID() + "." + fileExtension;
        // 指定视频保存路径
        String uploadDir = "D:\\cs学习";
        // 创建保存视频文件的目录
        File dir = new File(uploadDir);
        // 创建视频文件对象
        File videoFile = new File(dir.getAbsolutePath() + File.separator + fileName);
        //上传视频
        uploadVideoTask.uploadVideo(data,videoFile);
        String videoUrl = "http://localhost:8080/videos/" + fileName;
        String cover = UUID.randomUUID() + ".jpg";
        String coverUrl = "http://localhost:8080/videos/" + cover;
        //设置视频信息
        Video video = new Video();
        video.setUserId(userId);
        video.setVideoUrl(videoUrl);
        video.setCoverUrl(coverUrl);
        video.setTitle(title);
        video.setDescription(description);
        video.setCreateTime(new Date());
        //将视频上传到数据库
        videoMapper.insert(video);
        return new ResponseResult(Code.SUCCESS, Message.SUCCESS);
    }

    /**
     * 获取文件后缀
     */
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
     * 根据 userId 查看指定人的发布列表
     */
    @Override
    public ResponseResult listVideoByUserId(String userId, int pageNum, int pageSize) {
        //设置分页信息
        IPage page = new Page(pageNum, pageSize);
        LambdaQueryWrapper<Video> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Video::getUserId, userId);
        videoMapper.selectPage(page, lqw);
        List<Video> videoList = page.getRecords();
        if (videoList.isEmpty()){
            return new ResponseResult(Code.ERROR,Message.ERROR);
        }
        int total = (int)page.getTotal();
        if (pageNum > total || pageSize > total || pageNum < 0 || pageSize <= 0){
            throw  new BusinessException(Code.ERROR,Message.ERROR);
        }
        return new ResponseResult(Code.SUCCESS, Message.SUCCESS, new VideoDto(videoList,total));
    }

    /**
     * 根据关键字搜索视频
     */
    @Override
    public ResponseResult searchByKeyword(String keywords, int pageNum, int pageSize) {
        IPage page = new Page(pageNum, pageSize);
        LambdaQueryWrapper<Video> lqw = new LambdaQueryWrapper<>();
        //按照点击量排序
        if (keywords != null){
            lqw.like(Video::getTitle, keywords).or().like(Video::getDescription, keywords).orderByDesc(Video::getVisitCount);
        }
        videoMapper.selectPage(page, lqw);
        List<Video> videoList = page.getRecords();
        if (videoList.isEmpty()) {
            return new ResponseResult(Code.ERROR,Message.ERROR);
        }
        redisCache.setCacheObject("search:",keywords);
        int total = (int)page.getTotal();
        if (pageNum > total || pageSize > total || pageNum < 0 || pageSize <= 0){
            throw  new BusinessException(Code.ERROR,Message.ERROR);
        }
        return new ResponseResult(Code.SUCCESS, Message.SUCCESS, new VideoDto(videoList,(int)page.getTotal()));
    }

    /**
     * 查询播放量排行榜
     */
    @Override
    public ResponseResult popular() {
        String leaderboardKey = "leaderboard";
        Set<ZSetOperations.TypedTuple<String>> leaderboardPage = redisTemplate.opsForZSet().reverseRangeWithScores(leaderboardKey, 0L, 9);
        List<Video> videoList = new ArrayList<>();
        if(leaderboardPage == null){
            return new ResponseResult(Code.ERROR,Message.ERROR);
        }
        for (ZSetOperations.TypedTuple<String> tuple : leaderboardPage) {
            //redis根据点击量排序后得到排行榜，然后从redis中，根据pagesize和pagenum获取相应视频id
            String videoId =  tuple.getValue();
            // 根据视频ID从数据库中查询视频信息
            Video video = videoMapper.selectById(videoId);
            if (video != null) {
                videoList.add(video);
            }
        }
        if (videoList.isEmpty()){
            return new ResponseResult(Code.ERROR,Message.ERROR);
        }
        return new ResponseResult(Code.SUCCESS, Message.SUCCESS,new VideoDto(videoList,videoList.size()));
    }
}
