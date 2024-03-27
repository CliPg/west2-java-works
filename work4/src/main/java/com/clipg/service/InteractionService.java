package com.clipg.service;

import com.clipg.dto.ResponseResult;

/**
 * @author 77507
 */
public interface InteractionService {

    ResponseResult likeVideo(String actionType, String videoId) throws Exception;

    ResponseResult listVideoLikeByUserId(String userId);

    ResponseResult publishCommentToVideo(String videoId, String content) throws Exception;

    ResponseResult publishCommentToComment(String parentId, String content) throws Exception;

    ResponseResult commentList(String videoId, int pageNum, int pageSize);

    ResponseResult commentDelete(String token, String commentId) throws Exception;
}
