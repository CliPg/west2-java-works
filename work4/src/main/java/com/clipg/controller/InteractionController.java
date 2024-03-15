package com.clipg.controller;

import com.clipg.domain.ResponseResult;
import com.clipg.service.InteractionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class InteractionController {

    @Autowired
    InteractionService interactionService;

    @PostMapping("/like/action")
    public ResponseResult likeAction(@RequestParam("videoId")String videoId,
                                     @RequestParam("actionType")String actionType,
                                     @RequestHeader("token") String token){
        return interactionService.like(actionType, videoId, token);
    }

    @GetMapping("/like/list")
    public ResponseResult likeList(@RequestParam("userId")String userId){
        return interactionService.likeList(userId);
    }

    @PostMapping("/comment/publish")
    public ResponseResult commentPublish(@RequestParam("videoId")String videoId,
                                         @RequestParam("comment")String comment,
                                         @RequestHeader("token") String token){
        return interactionService.commentPublish(token, videoId, comment);
    }

    @GetMapping("/comment/list")
    public ResponseResult commentList(@RequestParam("videoId") String videoId,
                                      @RequestParam("pageNum") int pageNum,
                                      @RequestParam("pageSize") int pageSize){
        return interactionService.commentList(videoId, pageNum, pageSize);
    }

    @DeleteMapping("/comment/delete")
    public ResponseResult commentDelet(@RequestParam("videoId")String videoId,
                                       @RequestParam("comment")String comment,
                                       @RequestHeader("token") String token){
        return interactionService.commentDelete(token, videoId, comment);
    }


}
