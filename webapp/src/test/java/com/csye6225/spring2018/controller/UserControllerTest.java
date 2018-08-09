package com.csye6225.spring2018.controller;

import com.csye6225.spring2018.SpringBootWebApplicationTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class UserControllerTest extends SpringBootWebApplicationTest {

    @Autowired
    private UserController controller;


    @Test
    public void contexLoads() throws Exception {
        assertThat(controller).isNotNull();
    }


}