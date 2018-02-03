package com.csye6225.spring2018.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

  private final static Logger logger = LoggerFactory.getLogger(IndexController.class);
  
  @RequestMapping("/")
  public String index() {
    logger.info("Loading home page.");
    return "index";
  }

  @RequestMapping("/register")
  public String index1() {
    logger.info("Loading registration page.");
    return "register";
  }

  @RequestMapping("/login")
  public String login() {
    logger.info("Loading login page.");
    return "login";
  }

    @RequestMapping("/logout")
    public String logoutPage(){
        logger.info("Loading index page after logging out.");
        return "index";
    }

    @RequestMapping("/loginSuccess")
    public String userSucceess(){
        logger.info("Loading User Success Home page");
        return "UserHome";
    }
}
