package com.clipg.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.clipg.domain.ResponseResult;
import com.clipg.domain.Video;
import com.clipg.service.InteractionService;
import com.clipg.service.VideoService;
import com.clipg.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.resps.Tuple;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class InteractionServiceImpl implements InteractionService {

    @Autowired
    private VideoService videoService;

    @Resource
    StringRedisTemplate stringRedisTemplate;

    /**
     * 对视频进行点赞/取赞
     * @param actionType
     * @param videoId
     * @param token
     * @return
     */
    @Override
    public ResponseResult like(String actionType, String videoId, String token) {

        try {
            //获取登录用户
            Claims claims = JwtUtil.parseJWT(token);
            String userId = claims.getSubject();
            //判断当前用户是否已经点赞
            String key = "videoId:" + videoId + ":likes";
            boolean isMember =  stringRedisTemplate.opsForSet().isMember(key, userId.toString());
            if (BooleanUtils.isFalse(isMember) && "1".equals(actionType)){
                boolean isSuccess = videoService.update().setSql("like_count = like_count + 1").eq("id", videoId).update();
                if (isSuccess){
                    stringRedisTemplate.opsForSet().add(key, userId.toString());
                }

            }else if (!BooleanUtils.isFalse(isMember) && "2".equals(actionType)) {
                boolean isSuccess = videoService.update().setSql("like_count = like_count - 1").eq("id", videoId).update();
                if (isSuccess){
                    stringRedisTemplate.opsForSet().remove(key, userId.toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseResult(-1, "操作失败！");
        }
        return  new ResponseResult(200, "操作成功！");
    }

    /**
     * 返回指定用户点赞的视频
     * @param userId
     * @return
     */
    @Override
    public ResponseResult likeList(String userId) {
        String data = null;
        try {
            String keyPattern = "videoId:*:likes";
            List<Video> videos =new ArrayList<>();

            // 获取所有键匹配的视频点赞集合
            Set<String> keys = stringRedisTemplate.keys(keyPattern);

            // 遍历所有匹配的键
            for (String key : keys) {
                // 检查该用户是否点赞过当前视频
                if (stringRedisTemplate.opsForSet().isMember(key, userId)) {
                    // 从键中提取视频ID并添加到集合中
                    String videoId = key.split(":")[1]; // 视频ID位于键的第二部分
                    QueryWrapper queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", videoId);
                    Video video = videoService.getOne(queryWrapper);
                    videos.add(video);
                }
            }
            data = "items:" + videos;
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseResult(-1, "查询失败！");
        }
        return new ResponseResult(200, "查询成功！", data);


    }

    /**
     * 对视频进行评论
     * @param token
     * @param videoId
     * @param content
     * @return
     */
    @Override
    public ResponseResult commentPublish(String token, String videoId, String content) {

        try {
            // 获取登录用户
            Claims claims = JwtUtil.parseJWT(token);
            String userId = claims.getSubject();

            // 构建视频评论有序集合的键
            String key = "videoId:" + videoId + ":comments";

            // 获取当前时间戳
            long timestamp = System.currentTimeMillis();

            // 构建评论信息，包括用户ID和评论内容
            //String commentInfo = userId + ":" + timestamp + ":" + content;
            String commentInfo = userId + ":"  + content;

            // 添加评论到有序集合中，使用时间戳作为分数
            stringRedisTemplate.opsForZSet().add(key, commentInfo, timestamp);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseResult(-1, "评论失败！");
        } finally {
            return new ResponseResult(200, "评论成功！");
        }
    }

    /**
     * 获取视频的评论列表
     * @param videoId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public ResponseResult commentList(String videoId, int pageNum, int pageSize) {

        String data = null;
        String redisHost = "localhost";
        int redisPort = 6379;
        // 创建 Jedis 连接池配置
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        JedisPool jedisPool = new JedisPool(poolConfig, redisHost, redisPort);
        try(Jedis jedis = jedisPool.getResource()) {
            // 构建视频评论有序集合的键
            String key = "videoId:" + videoId + ":comments";

            // 从 Redis 缓存中获取视频的评论
            List<Tuple> comments =  jedis.zrevrangeWithScores(key, (pageNum-1) * pageSize, pageNum * pageSize - 1);

            // 构建评论列表
            List<String> commentList = new ArrayList<>();
            for (Tuple tuple : comments) {
                String comment = tuple.getElement();
                commentList.add(comment);
            }
            data = "items:" + commentList;

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseResult(-1, "查询失败！");
        }

        return new ResponseResult(200, "查询成功！", data);

    }

    /**
     * 用户删除自己已发表的评论
     * @param token
     * @param videoId
     * @param comment
     * @return
     */
    @Override
    public ResponseResult commentDelete(String token, String videoId, String comment) {
        try {
            // 获取登录用户
            Claims claims = JwtUtil.parseJWT(token);
            String userId = claims.getSubject();

            // 构建视频评论有序集合的键
            String key = "videoId:" + videoId + ":comments";

            // 构建评论信息，包括用户ID和评论内容
            // 这里假设评论信息的格式为 "userId:timestamp:content"
            String commentInfo = userId + ":"  + comment;

            // 从 Redis 缓存中移除指定评论
            long removed = stringRedisTemplate.opsForZSet().remove(key, commentInfo);

            // 判断评论是否删除成功
            if (removed > 0) {
                return new ResponseResult(200, "评论删除成功！");
            } else {
                return new ResponseResult(-1, "评论不存在或已被删除！");
            }
        } catch (Exception e) {
            throw new RuntimeException("删除评论失败！", e);
        }
    }

}
