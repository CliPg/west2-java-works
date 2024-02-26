package com.clipg.service;

import com.clipg.domain.User;


import java.util.List;

public interface UserService {

    public void save(User user);

    public void update(User user);

    public void delete(String id);

    public User getById(String id);

    public List<User> listUser();
}
