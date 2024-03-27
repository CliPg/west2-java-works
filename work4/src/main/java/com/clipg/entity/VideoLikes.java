package com.clipg.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 77507
 */
@TableName("video_likes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VideoLikes {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    private String userId;
    private String videoId;


}
