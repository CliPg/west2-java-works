package com.clipg.service;

import com.clipg.dto.ResponseResult;

/**
 * @author 77507
 */
public interface InteractionService {

    ResponseResult likeVideo(String actionType, String videoId);

    ResponseResult listVideoLikeByUserId(String userId);

    ResponseResult publishCommentToVideo(String videoId, String content);

    ResponseResult publishCommentToComment(String parentId, String content);

    ResponseResult commentList(String videoId, int pageNum, int pageSize);

    ResponseResult commentDelete(String commentId);
}
