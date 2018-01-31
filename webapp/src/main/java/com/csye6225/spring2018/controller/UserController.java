package com.csye6225.spring2018.controller;

import com.csye6225.spring2018.GenericResponse;
import com.csye6225.spring2018.UserRepository;
import com.csye6225.spring2018.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
<<<<<<< HEAD
import org.springframework.session.Session;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.Date;
=======
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
>>>>>>> b744cb92f7c4f18270066dc27d69e163cba0cde3


@RestController
public class UserController {
    private final static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository;
    HttpSession session;


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

    @RequestMapping(value = "/registerUser", method = RequestMethod.POST)
    @ResponseBody
    public GenericResponse createUser(@Valid User user, HttpServletRequest request){
        logger.info("Registering user account with information: {}", user);
        User newUser = new User();
        newUser.setEmail(String.valueOf(user.getEmail()));
        newUser.setPassword(BCrypt.hashpw(String.valueOf(user.getPassword()),BCrypt.gensalt()));
        userRepository.save(newUser);
        String appUrl = "http://" + request.getServerName() + ":" +
                request.getServerPort() + request.getContextPath();

        return new GenericResponse("success");
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
