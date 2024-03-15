package com.clipg.service;

import com.clipg.domain.ResponseResult;

public interface SocialService {

    ResponseResult follow(String token, String followingId, int actionType);

    ResponseResult followingList(String userId, int pageNum, int pageSize);
    ResponseResult followerList(String userId, int pageNum, int pageSize);

    public ResponseResult friendsList(String token, int pageNum, int pageSize);
}
