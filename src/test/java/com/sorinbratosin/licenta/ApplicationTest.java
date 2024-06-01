package com.sorinbratosin.licenta;

import com.sorinbratosin.licenta.Controller.UserController;
import com.sorinbratosin.licenta.Service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ApplicationTest {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserController userController;

    @Test
    public void contextLoads() {
        assertThat(jwtService).isNotNull();
        assertThat(userController).isNotNull();
    }
}

