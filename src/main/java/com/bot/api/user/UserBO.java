package com.bot.api.user;

import com.bot.api.core.UserMapper;
import com.bot.api.model.common.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserBO {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserDAO userDAO;

    public void insertUser() {

    }

    public User selectUser(String userKey) {
        User user = new User();
        return userDAO.selectUser(userKey);
    }
}