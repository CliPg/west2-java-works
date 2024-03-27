package com.clipg.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * @author 77507
 */
@Slf4j
@Component
public class UploadVideoTask {
    @Async
    public void uploadVideo(MultipartFile data, File videoFile) throws IOException {
        // 将上传的视频文件保存到本地
        data.transferTo(videoFile);
        log.info("上传成功");
    }
}
