package com.clipg.task;

import com.clipg.entity.Video;
import com.clipg.mapper.VideoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

/**
 * @author 77507
 */
@Configuration
@EnableScheduling
public class ScheduledTask {

    @Autowired
    private VideoMapper videoMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 更新排行榜
     * 每小时更新一次
     */
    @Scheduled(fixedRate = 3600000)
    public void updateLeaderboard(){
        List<Video> videos = videoMapper.selectList(null);

        for (Video video : videos) {
            String videoId = video.getId();
            int visitCount = video.getVisitCount();
            // 将视频数据存储到 Redis 中
            String leaderboardKey = "leaderboard";
            redisTemplate.opsForZSet().add(leaderboardKey, videoId, visitCount);
        }
    }
}
