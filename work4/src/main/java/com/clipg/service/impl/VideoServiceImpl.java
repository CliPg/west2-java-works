package com.clipg.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.clipg.dao.VideoDao;
import com.clipg.domain.Datas;
import com.clipg.domain.ResponseResult;

import com.clipg.domain.Video;
import com.clipg.service.VideoService;
import com.clipg.util.FastJsonRedisSerializer;
import com.clipg.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.resps.Tuple;


import java.util.*;


@Service
public class VideoServiceImpl extends ServiceImpl<VideoDao, Video> implements VideoService {

    @Autowired
    private VideoDao videoDao;

    /**
     * 上传视频
     *
     * @param token
     * @param video
     * @return
     */
    @Override
    public ResponseResult publish(String token, Video video) {

        try {
            Date date = new Date();
            String userId;
            //从token中获取用户id
            Claims claims = JwtUtil.parseJWT(token);
            userId = claims.getSubject();
            //设置视频信息
            Video uploadVideo = new Video();
            uploadVideo.setUserId(userId);
            uploadVideo.setVideoUrl(video.getVideoUrl());
            uploadVideo.setCoverUrl(video.getCoverUrl());
            uploadVideo.setTitle(video.getTitle());
            uploadVideo.setDescription(video.getDescription());
            uploadVideo.setCreateTime(date);
            videoDao.insert(uploadVideo);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseResult(-1, "上传失败！");
        }

        return new ResponseResult(200, "上传成功！");

    }

    /**
     * 根据 userId 查看指定人的发布列表
     *
     * @param userId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public ResponseResult list(String userId, int pageNum, int pageSize) {

        Datas data = new Datas<>();
        try {
            IPage page = new Page(pageNum, pageSize);
            LambdaQueryWrapper<Video> lqw = new LambdaQueryWrapper<>();
            lqw.eq(Video::getUserId, userId);
            videoDao.selectPage(page, lqw);
            int total = (int) page.getTotal();
            List items = page.getRecords();
            //data = "items:" + items + ", total:" + total;
            data.setItems(items);
            data.setTotal(total);
            if (items.isEmpty()) {
                return new ResponseResult(-1, "该用户还未过发表视频！");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseResult(-1, "查询失败！");
        }

        return new ResponseResult(200, "查询成功！", data);


    }

    /**
     * 根据关键字搜索视频
     * @param keywords
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public ResponseResult search(String keywords, int pageNum, int pageSize) {


        Datas data = new Datas<>();
        try {
            IPage page = new Page(pageNum, pageSize);
            LambdaQueryWrapper<Video> lqw = new LambdaQueryWrapper<>();
            lqw.like(Video::getTitle, keywords).or().like(Video::getDescription, keywords);
            videoDao.selectPage(page, lqw);
            List items = page.getRecords();
            int total = (int) page.getTotal();
            //data = "items:" + items + ", total:" + total;
            data.setItems(items);
            data.setTotal(total);
            if (items.isEmpty()) {
                return new ResponseResult(-1, "暂无相关视频！");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseResult(-1, "查询失败！");
        }

        return new ResponseResult(200, "查询成功！", data);

    }

    /**
     * 查询播放量排行榜
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public ResponseResult popular(int pageNum, int pageSize) {

        List<Video> videoList = new ArrayList<>();
        String redisHost = "localhost";
        int redisPort = 6379;
        Datas data =new Datas<>();
        // 创建 Jedis 连接池配置
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        JedisPool jedisPool = new JedisPool(poolConfig, redisHost, redisPort);

        try (Jedis jedis = jedisPool.getResource()) {
            // 从 VideoDao 获取数据库中的视频对象列表
            List<Video> videos = videoDao.selectList(null);
            String leaderboardKey = "leaderboard";
            for (Video video : videos) {
                // 将视频对象序列化为 JSON 字符串
                //byte[] serializedVideo = serializer.serialize(video);
                // 假设视频对象有一个唯一的标识符，比如 ID

                String videoId = video.getId();
                int visitCount = video.getVisitCount();
                // 将视频数据存储到 Redis 中
                jedis.zadd(leaderboardKey, visitCount, videoId);
            }

            //获取指定页排行榜数据
            List<Tuple> leaderboardPage =  jedis.zrevrangeWithScores(leaderboardKey, (pageNum-1) * pageSize, pageNum * pageSize - 1);
            //List<Video> videoList = new ArrayList<>();
            for (Tuple tuple : leaderboardPage) {
                String videoId = tuple.getElement();
                QueryWrapper queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("id", videoId);
                Video video = videoDao.selectOne(queryWrapper);
                videoList.add(video);
            }
            int total = videos.size();
            data.setItems(videoList);
            data.setTotal(total);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseResult(-1, "查询失败！");
        } finally {
            // 关闭 Jedis 连接池
            jedisPool.close();
        }

        //return new ResponseResult(200, "查询成功！",data);
        return new ResponseResult(200, "查询成功！",data);

    }

}
