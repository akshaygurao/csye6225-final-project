package com.csye6225.spring2018.controller;

import com.csye6225.spring2018.UserRepository;
import com.csye6225.spring2018.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.session.Session;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.Date;


@Controller
public class UserController {
    private final static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository;
    HttpSession session;

    @RequestMapping("/register")
    public String viewRegisterPage() {
        logger.info("Loading registration page.");
        return "register";
    }

    @RequestMapping("/loginUser")
    @PostMapping("/loginUser")
    public String login(@RequestParam String email, @RequestParam String password) {

        for (User u : userRepository.findAll()){
        if (u.getEmail().equals(email) && BCrypt.checkpw(password,u.getPassword())){
        logger.info("Loading user home page.");
        System.out.println("Found!");
        session.setAttribute("user",email);
            return "UserHome";
        }
        }
        System.out.println("User Not found");
        return "error";
    }

    @PostMapping("/registerUser")
    public String createUser(@RequestParam String email, @RequestParam String password) {
        logger.info("Loading index page after successful registration.");
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setPassword(BCrypt.hashpw(password,BCrypt.gensalt()));
        userRepository.save(newUser);
        return "index";
    }

    @RequestMapping("/login")
    public String viewLoginPage(){
        logger.info("Loading login page.");
        return "login";
    }

    @RequestMapping("/logout")
    public String logoutPage(HttpSession session){
        logger.info("Loading index page after logging out.");
        session.invalidate();
        return "index";
    }
}
