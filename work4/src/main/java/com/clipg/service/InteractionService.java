package com.clipg.service;

import com.clipg.domain.ResponseResult;

public interface InteractionService {

    ResponseResult like(String actionType, String videoId, String token);

    ResponseResult likeList(String userId);

    ResponseResult commentPublish(String token, String videoId, String content);

    ResponseResult commentList(String videoId, int pageNum, int pageSize);

    ResponseResult commentDelete(String token, String videoId, String comment);
}
