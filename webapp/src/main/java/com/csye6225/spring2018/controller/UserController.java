package com.csye6225.spring2018.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class UserController {
    private final static Logger logger = LoggerFactory.getLogger(UserController.class);

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

    @RequestMapping(value = "/registerUser" , method = RequestMethod.POST)
    public String index2() {
        logger.info("Loading index page after successful registration.");
        return "index";
    }
}
