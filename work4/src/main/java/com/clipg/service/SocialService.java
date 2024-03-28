package com.clipg.service;

import com.clipg.dto.ResponseResult;
/**
 * @author 77507
 */
public interface SocialService {
    ResponseResult follow(String followingId, int actionType);
    ResponseResult followingList(String userId, int pageNum, int pageSize);
    ResponseResult followerList(String userId, int pageNum, int pageSize);
    ResponseResult friendsList(int pageNum, int pageSize) throws Exception;
}
