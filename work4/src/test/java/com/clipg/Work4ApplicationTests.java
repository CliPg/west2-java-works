package com.clipg;

import com.clipg.dao.UserDao;
import com.clipg.domain.User;
import com.clipg.service.InteractionService;
import com.clipg.service.UserService;
import com.clipg.service.VideoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest
class Work4ApplicationTests {

    //@Autowired
    //private UserDao userDao;

    @Autowired
    private UserService userService;

    @Autowired
    private VideoService videoService;

    @Autowired
    private InteractionService interactionService;
//    @Test
//    public void test(){
//        System.out.println(userDao.selectById("1"));;
//    }

    @Test
    public void like(){
//        interactionService.like("1","mormig");
    }

    @Test
    public void save(){
        //User user = new User();
        //user.setUsername("ppggg");
        //user.setPassword("123456");
        //userService.register(user);
    }

    @Test
    void testSave() throws  ParseException {
        //String dateString = "2024-02-28"; // 例如 "2024-02-28" 格式的日期字符串
        //DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //Date date = dateFormat.parse(dateString);
        Date date = new Date();

        User user = new User();
        user.setUsername("clippggg");
        user.setPassword("123456");
        user.setCreateTime(date);
        //user.setUpdateTime(date);
        //user.setDeleteTime(date);
        //user.setAvatarUrl("www");
        userService.save(user);
        System.out.println(user.getId());
    }

    public boolean exception(){

        boolean flag = true;
        try {
            int i = 1/0;
        } catch (Exception e) {
            flag = false;
            throw new RuntimeException(e);
        }finally {
            return flag;
        }


    }

    @Test
    public void exc(){
        System.out.println(exception());
    }

}
