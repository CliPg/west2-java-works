package com.clipg.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * @author 77507
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("comment")
public class Comment {

    @TableId(type = IdType.ASSIGN_ID)

    private String id;

    private String userId;

    private String videoId;

    private String content;

    private String parentId;

    private int likeCount;

    private int childCount;

    private Date createTime;

    private Date updateTime;

    private Date deleteTime;

}
