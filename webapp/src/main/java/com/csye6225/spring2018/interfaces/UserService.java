package com.csye6225.spring2018.interfaces;


import org.springframework.security.core.userdetails.UserDetailsService;
import com.csye6225.spring2018.model.User;
import com.csye6225.spring2018.DTO.UserRegistrationDto;

public interface UserService extends UserDetailsService {

    User findByEmail(String email);

    User save(UserRegistrationDto registration);
}
