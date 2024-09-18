package com.clipg.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.common.auth.CredentialsProviderFactory;
import com.aliyun.oss.common.auth.EnvironmentVariableCredentialsProvider;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.clipg.dto.*;
import com.clipg.exception.BusinessException;
import com.clipg.mapper.VideoMapper;

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
import java.io.FileInputStream;
import java.io.InputStream;
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

        // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
        String endpoint = "https://oss-cn-heyuan.aliyuncs.com";
        String accessKeyId = "LTAI5tRJ8Xz6h5QY2XJ7Xx6G";
        String accessKeySecret = "w8z9rX6j3f1m3y3k6h4l2i5n0j7o4q8p";

        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        InputStream inputStream = new FileInputStream(videoFile);
        ossClient.putObject("clipg-work4-videos", fileName, inputStream);

        ossClient.shutdown();


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
        LambdaQueryWrapper<Video> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Video::getUserId, userId);
        Page<Video> page = new Page<>(pageNum, pageSize);
        IPage<Video> videoPage = videoMapper.selectPage(page, lqw);
        List<Video> videoList = videoPage.getRecords();
        int total = (int) videoPage.getTotal();
        if (pageNum > total || pageSize > total){
            throw  new BusinessException(Code.ERROR,Message.ERROR);
        }
        if (videoList.isEmpty()) {
            return new ResponseResult(Code.ERROR, Message.ERROR);
        }
        return new ResponseResult(Code.SUCCESS, Message.SUCCESS, new VideoDto(page.getRecords(), total));
    }


    /**
     * 根据关键字搜索视频
     */
    @Override
    public ResponseResult searchByKeyword(String keywords, int pageNum, int pageSize) {
        String userId = userHolder.getUserId();
        LambdaQueryWrapper<Video> lqw = new LambdaQueryWrapper<>();
        //按照点击量排序
        if (keywords != null){
            lqw.like(Video::getTitle, keywords).or().like(Video::getDescription, keywords).orderByDesc(Video::getVisitCount);
        }
        Page<Video> page = new Page<>(pageNum, pageSize);
        IPage<Video> videoPage = videoMapper.selectPage(page, lqw);
        List<Video> videoList = videoPage.getRecords();
        int total = (int) videoPage.getTotal(); // 获取总记录数
        if (pageNum > total || pageSize > total){
            throw  new BusinessException(Code.ERROR,Message.ERROR);
        }
        if (videoList.isEmpty()) {
            return new ResponseResult(Code.ERROR, Message.ERROR);
        }
        redisCache.setCacheObject("search:" + keywords, userId);
        return new ResponseResult(Code.SUCCESS, Message.SUCCESS, new VideoDto(page.getRecords(), total));
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
