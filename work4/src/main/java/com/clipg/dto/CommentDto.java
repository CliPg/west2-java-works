package com.clipg.dto;

import com.clipg.entity.Comment;
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
public class CommentDto {
    private List<Comment> items;
    private int total;
}
