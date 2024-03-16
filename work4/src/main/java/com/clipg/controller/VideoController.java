package com.clipg.controller;

import com.clipg.domain.ResponseResult;
import com.clipg.domain.User;
import com.clipg.domain.Video;
import com.clipg.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/video")
public class VideoController {

    @Autowired
    private VideoService videoService;

    @PostMapping("/publish")
    public ResponseResult publish(@RequestBody Video video,
                                  @RequestHeader("token") String token){
        return videoService.publish(token, video);
    }

    @GetMapping("/list")
    public ResponseResult list(@RequestParam("userId") String userId,
                               @RequestParam("pageNum") int pageNum,
                               @RequestParam("pageSize") int pageSize){
        return videoService.list(userId,pageNum,pageSize);
    }

    @PostMapping("/search")
    public ResponseResult search(@RequestParam("keywords") String keywords,
                                 @RequestParam("pageNum") int pageNum,
                                 @RequestParam("pageSize") int pageSize){
        return videoService.search(keywords,pageNum,pageSize);
    }

    @GetMapping("/popular")
    public ResponseResult popular(@RequestParam("pageNum") int pageNum,
                                  @RequestParam("pageSize") int pageSize){
        return videoService.popular(pageNum,pageSize);
    }
}
