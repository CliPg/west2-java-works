package com.clipg.controller;

import com.clipg.dto.ResponseResult;
import com.clipg.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author 77507
 */
@RestController
@RequestMapping("/video")
public class VideoController {

    @Autowired
    private VideoService videoService;

    @PostMapping("/publish")
    public ResponseResult publishVideo(@RequestParam("video") MultipartFile data,
                                       @RequestParam("title") String title,
                                       @RequestParam("description") String description) throws Exception {
        return videoService.publishVideo(data, title, description);
    }

    @GetMapping("/list")
    public ResponseResult listVideoByUserId(@RequestParam("userId") String userId,
                                            @RequestParam("pageNum") int pageNum,
                                            @RequestParam("pageSize") int pageSize){
        return videoService.listVideoByUserId(userId,pageNum,pageSize);
    }

    @PostMapping("/search")
    public ResponseResult searchByKeyword(@RequestParam("keywords") String keywords,
                                          @RequestParam("pageNum") int pageNum,
                                          @RequestParam("pageSize") int pageSize){
        return videoService.searchByKeyword(keywords,pageNum,pageSize);
    }

    @GetMapping("/popular")
    public ResponseResult popular(){
        return videoService.popular();
    }
}
