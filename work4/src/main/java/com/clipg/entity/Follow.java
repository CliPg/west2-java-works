package com.clipg.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author 77507
 */
@TableName("follow")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Follow implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String followerId;

    private String followingId;

    public Follow(String followerId, String followingId) {
        this.followerId = followerId;
        this.followingId = followingId;
    }
}
