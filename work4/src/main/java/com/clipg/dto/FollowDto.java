package com.clipg.dto;

import com.clipg.entity.Follow;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author 77507
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FollowDto {
    private List<UserDto> items;
    private int total;
}
