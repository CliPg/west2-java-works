package com.clipg.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.clipg.domain.ResponseResult;
import com.clipg.domain.Video;

public interface VideoService extends IService<Video> {

    ResponseResult publish(String token, Video video);

    ResponseResult list(String userId, int pageNum, int pageSize);

    ResponseResult search(String keywords, int pageNum, int pageSize);

    ResponseResult popular(int pageNum, int pageSize);
}
