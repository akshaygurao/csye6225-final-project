package com.csye6225.spring2018.controller;

import com.csye6225.spring2018.DAO.UserDAO;
import com.csye6225.spring2018.pojo.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.validation.Valid;


@Controller
public class UserController {
    private final static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserDAO userDao;

    @RequestMapping("/register")
    public String index() {
        logger.info("Loading registration page.");
        return "register";
    }

    @RequestMapping("/user")
    public String index1() {
        logger.info("Loading user home page.");
        return "UserHome";
    }

    @PostMapping("/registerUser")
    public String createUser(@Valid @RequestParam String email, @RequestParam String password) {
        logger.info("Loading index page after successful registration.");
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        try {
            userDao.register(user);
            logger.info("User added to database successfully");
        } catch (Exception e) {
            logger.info("Error adding user to database");
            e.printStackTrace();
        }
        return "index";
    }
}
