package com.clipg.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@TableName("follow")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Follow implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String followerId;

    private String followingId;


}
