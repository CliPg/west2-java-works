package com.clipg.controller;

import com.clipg.dto.ResponseResult;
import com.clipg.service.InteractionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author 77507
 */
@RestController
public class InteractionController {

    @Autowired
    InteractionService interactionService;

    @PostMapping("/like/action")
    public ResponseResult likeAction(@RequestParam("videoId")String videoId,
                                     @RequestParam("actionType")String actionType) throws Exception {
        return interactionService.likeVideo(actionType, videoId);
    }

    @GetMapping("/like/list")
    public ResponseResult likeList(@RequestParam("userId")String userId){
        return interactionService.listVideoLikeByUserId(userId);
    }

    @PostMapping("/comment/publish/toVideo")
    public ResponseResult commentPublishToVideo(@RequestParam("videoId")String videoId,
                                         @RequestParam("content")String content) throws Exception {
        return interactionService.publishCommentToVideo(videoId, content);
    }

    @PostMapping("/comment/publish/toComment")
    public ResponseResult commentPublishToComment(@RequestParam("parentId")String parentId,
                                         @RequestParam("content")String content) throws Exception {
        return interactionService.publishCommentToComment(parentId, content);
    }

    @GetMapping("/comment/list")
    public ResponseResult commentList(@RequestParam("videoId") String videoId,
                                      @RequestParam("pageNum") int pageNum,
                                      @RequestParam("pageSize") int pageSize){
        return interactionService.commentList(videoId, pageNum, pageSize);
    }

    @DeleteMapping("/comment/delete")
    public ResponseResult commentDelet(@RequestParam("commentId")String commentId,
                                       @RequestHeader("token") String token) throws Exception {
        return interactionService.commentDelete(token,commentId);
    }


}
