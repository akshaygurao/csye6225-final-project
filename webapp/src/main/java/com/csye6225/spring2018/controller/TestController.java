package com.csye6225.spring2018.controller;

import com.csye6225.spring2018.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/user")
    public User firstPage(){
        User user = new User();
        user.setFirstname("Akshay");
        user.setLastname("Gurao");
        user.setEmail("akshaygurao@gmail.com");
        user.setPassword("abc");
        return user;
    }
}
