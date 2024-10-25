package com.clipg.task;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.clipg.dto.Code;
import com.clipg.dto.Message;
import com.clipg.entity.Video;
import com.clipg.exception.BusinessException;
import com.clipg.mapper.VideoMapper;
import com.clipg.util.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

/**
 * @author 77507
 */
@Slf4j
@Component
public class UploadVideoTask {

    @Autowired
    private VideoMapper videoMapper;


    @Async
    public void uploadVideo(MultipartFile data,String fileName) throws IOException {



        String endpoint = "https://oss-cn-heyuan.aliyuncs.com";
        String accessKeyId = "LTAI5tKES8FuAWyx66hLM8um";
        String accessKeySecret = "14fausF8C3PuUemkQSgEeiLjEGq2Hy";

        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        InputStream inputStream = data.getInputStream();
        ossClient.putObject("clipg-work4-videos", fileName, inputStream);

        ossClient.shutdown();

    }


}
