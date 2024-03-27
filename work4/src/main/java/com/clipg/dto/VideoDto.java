package com.clipg.dto;

import com.clipg.entity.Video;
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
public class VideoDto {
    private List<Video> items;
    private int total;
}
