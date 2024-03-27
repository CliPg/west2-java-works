package com.clipg.controller;


import com.clipg.dto.ResponseResult;
import com.clipg.service.SocialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author 77507
 */
@RestController
public class SocialController {

    @Autowired
    private SocialService socialService;

    @PostMapping("/relation/action")
    public ResponseResult follow(@RequestParam("followingId") String followingId,
                                 @RequestParam("actionType") int actionType) throws Exception {
        return socialService.follow(followingId,actionType);
    }

    @GetMapping("/following/list")
    public ResponseResult followingList(@RequestParam("userId") String userId,
                                        @RequestParam("pageNum") int pageNum,
                                        @RequestParam("pageSize") int pageSize){
        return socialService.followingList(userId, pageNum, pageSize);
    }

    @GetMapping("/follower/list")
    public ResponseResult followerList(@RequestParam("userId") String userId,
                                        @RequestParam("pageNum") int pageNum,
                                        @RequestParam("pageSize") int pageSize){
        return socialService.followerList(userId, pageNum, pageSize);
    }

    @GetMapping("/friends/list")
    public ResponseResult friendsList(@RequestParam("pageNum") int pageNum,
                                       @RequestParam("pageSize") int pageSize) throws Exception {
        return socialService.friendsList(pageNum, pageSize);
    }

}
