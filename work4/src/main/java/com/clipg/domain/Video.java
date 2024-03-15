package com.clipg.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
@TableName("videos")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Video implements Serializable {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String userId;

    private String videoUrl;

    private String coverUrl;

    private String title;

    private String description;

    private int visitCount;

    private int likeCount;

    private int commentCount;

    private Date createTime;

    private Date updateTime;

    private Date deleteTime;



}
