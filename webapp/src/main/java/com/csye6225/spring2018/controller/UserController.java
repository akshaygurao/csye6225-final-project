package com.csye6225.spring2018.controller;

import com.csye6225.spring2018.GenericResponse;
import com.csye6225.spring2018.UserRepository;
import com.csye6225.spring2018.User;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.xml.ws.Response;
import java.util.HashMap;


@RestController
public class UserController {
    private final static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository;


    @PostMapping("/loginUser")
    @ResponseBody
    public String login(@RequestBody HashMap<String, String> map) {
        for (User u : userRepository.findAll()){
        if (u.getEmail().equals(map.get("email")) && BCrypt.checkpw(map.get("password"),u.getPassword())){
        logger.info("Loading user home page.");
        System.out.println("Found!");
            return "UserHome";
        }
        }
        System.out.println("User Not found");
        return "error";
    }

    @PostMapping("/registerUser")
    @ResponseBody
    public String createUser(@RequestBody HashMap<String,String> js) throws JSONException {
        logger.info("Registering user account with information: {}");
        System.out.println(js.toString());
        System.out.println(js);
        User newUser = new User();
        newUser.setEmail(String.valueOf(js.get("email")));
        newUser.setPassword(BCrypt.hashpw(String.valueOf(js.get("password")),BCrypt.gensalt()));
        userRepository.save(newUser);
        return "Success!";
        }


    @RequestMapping("/logout")
    public String logoutPage(){
        logger.info("Loading index page after logging out.");
        return "index";
    }
}
