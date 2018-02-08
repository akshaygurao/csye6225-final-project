package com.csye6225.spring2018.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class IndexController {

  private final static Logger logger = LoggerFactory.getLogger(IndexController.class);
  
  @RequestMapping(value = "/", method = RequestMethod.GET)
  public String index() {
    logger.info("Loading home page.");
    return "index";
  }

  @RequestMapping(value = "/register", method = RequestMethod.GET)
  public String index1() {
    logger.info("Loading registration page.");
    return "register";
  }

  @RequestMapping(value = "/login", method = RequestMethod.GET)
  public String login() {
    logger.info("Loading login page.");
    return "login";
  }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logoutPage(HttpServletRequest request){
        logger.info("Loading index page after logging out.");
        request.getSession().invalidate();
        return "index";
    }

    @RequestMapping(value = "/loginSuccess", method = RequestMethod.GET)
    public String userSucceess(HttpServletRequest request){
        logger.info("Loading User Success Home page");
      /*HttpSession session = request.getSession(false);
      if(session == null){
        return "login";
      }*/
        return "UserHome";
    }
}
