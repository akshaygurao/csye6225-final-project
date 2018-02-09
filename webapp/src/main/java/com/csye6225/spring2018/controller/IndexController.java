package com.csye6225.spring2018.controller;

import com.csye6225.spring2018.User;
import com.csye6225.spring2018.UserRepository;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
public class IndexController {

  @Autowired
  private UserRepository userRepository;

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

  @RequestMapping(value = "/login", method = RequestMethod.POST)
  public String postLogin() {
    logger.info("Loading login after registration.");
    return "login";
  }

  @RequestMapping(value = "/logout", method = RequestMethod.GET)
  public String logoutPage(HttpServletRequest request){
    logger.info("Loading index page after logging out.");
    request.getSession().invalidate();
    return "index";
  }

  @RequestMapping(value = "/loginSuccess", method = RequestMethod.POST)
  public String userSucceess(HttpServletRequest request, @RequestParam String email, @RequestParam String password){
    logger.info("Loading User Success Home page");
    for (User u : userRepository.findAll()) {
      if (u.getEmail().equals(email) && BCrypt.checkpw(password, u.getPassword())) {
        logger.info("Login Success");
        HttpSession session = request.getSession(true);
        session.setAttribute("email", email);
        return "UserHome";
      }
    }
    return "login";
  }

  @RequestMapping(value = "/loginSuccess", method = RequestMethod.GET)
  public String userSuccessGet(HttpServletRequest request){
    HttpSession session = request.getSession(false);
    if (session == null){
      logger.info("Redirecting to login as no session found");
      return "login";
    }
    logger.info("Loading user homepage since session is present");
    return "UserHome";
  }
}
