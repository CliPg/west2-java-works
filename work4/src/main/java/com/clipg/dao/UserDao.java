package com.clipg.dao;

import com.clipg.domain.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserDao {

    @Insert("insert into bullet_screen_video_website.users (username,password,avatar_url,create_time,update_time,delete_time) values(#{username},#{password},#{avatarUrl},#{createTime},#{updateTime},#{deleteTime})")
    public void save(User user);

    @Update("update bullet_screen_video_website.users set username = #{username}, password = #{password}, avatar_url = #{avatarUrl}, create_time = #{createTime}, update_time = #{updateTime}, deleteTime = #{deleteTime} where id = #{id}")
    public void update(User user);

    @Delete("delete from bullet_screen_video_website.users where id = #{id}")
    public void delete(String id);

    @Select("select  * from bullet_screen_video_website.users where id = #{id}")
    public User getById(String id);

    @Select("select * from bullet_screen_video_website.users")
    public List<User> listUser();
}
