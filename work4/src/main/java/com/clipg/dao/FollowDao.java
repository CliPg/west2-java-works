package com.clipg.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clipg.domain.Follow;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FollowDao extends BaseMapper<Follow> {
}