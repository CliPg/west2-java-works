package com.clipg.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author 77507
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String id;
    private String username;
    private String avatarUrl;
    private Date createTime;
    private Date updateTime;
    private Date deleteTime;

}
