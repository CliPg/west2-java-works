package com.clipg.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.clipg.dto.ResponseResult;
import com.clipg.entity.Video;
import org.springframework.web.multipart.MultipartFile;
/**
 * @author 77507
 */
public interface VideoService extends IService<Video> {

    ResponseResult publishVideo(MultipartFile data, String title, String description) throws Exception;

    ResponseResult listVideoByUserId(String userId, int pageNum, int pageSize);

    ResponseResult searchByKeyword(String keywords, int pageNum, int pageSize);

    ResponseResult popular();
}
