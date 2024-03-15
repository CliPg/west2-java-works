package com.clipg.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clipg.domain.User;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface UserDao extends BaseMapper<User> {

}
