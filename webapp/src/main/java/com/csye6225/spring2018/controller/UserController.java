package com.csye6225.spring2018.controller;

import com.csye6225.spring2018.UserRepository;
import com.csye6225.spring2018.User;

import com.google.gson.JsonObject;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;


@Controller
public class UserController {
    private final static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository;


    @RequestMapping(value="/loginUser", consumes = "application/json", produces = "application/json", method = RequestMethod.POST)
    @ResponseBody
    public String login(@RequestBody String js, HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {

        JSONParser parser = new JSONParser();
        HashMap<String, String> map = (HashMap<String, String>) parser.parse(js);
        for (User u : userRepository.findAll()) {
            if (u.getEmail().equals(map.get("email")) && BCrypt.checkpw(map.get("password"), u.getPassword())) {
                logger.info("Loading user home page.");
                HttpSession session = request.getSession(true);
                session.setAttribute("email", map.get("email"));
                System.out.println("Found!");
                JsonObject json = new JsonObject();
                json.addProperty("response", "Login Success!");
                try {
                    response.sendRedirect("/loginSuccess");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return json.toString();
            }
        }
        System.out.println("User Not found");
        return new JsonObject().toString();
    }

    @RequestMapping(value = "/registerUser", consumes = "application/json", produces = "application/json", method = RequestMethod.POST)
    @ResponseBody
    public String createUser(@RequestBody String js) throws ParseException {
        logger.info("Registering user account with information: {}");
        System.out.println(js);
        JSONParser parser = new JSONParser();
        HashMap<String, String> map = (HashMap<String, String>) parser.parse(js);
        for (User u : userRepository.findAll()){
            if (u.getEmail().equals(map.get("email"))) {
                logger.info("User already exists.");
                return "error";
            }
        }
        System.out.println(map.toString());
        System.out.println(map);
        User newUser = new User();
        newUser.setEmail(String.valueOf(map.get("email")));
        newUser.setPassword(BCrypt.hashpw(String.valueOf(map.get("password")),BCrypt.gensalt()));
        userRepository.save(newUser);
        JsonObject json = new JsonObject();
        json.addProperty("response","Success!");
        return json.toString();
    }
}

