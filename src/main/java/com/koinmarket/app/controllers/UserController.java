package com.koinmarket.app.controllers;
import com.koinmarket.app.entities.JwtToken;
import com.koinmarket.app.requestBodies.LoginRequestBody;
import com.koinmarket.app.requestBodies.RegisterRequestBody;
import com.koinmarket.app.services.MyUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    private MyUserService myUserService;

    @PostMapping("/register")
    public JwtToken register(@RequestBody RegisterRequestBody registerRequestBody) {
        return myUserService.register(registerRequestBody);
    }

    @PostMapping("/login")
    public JwtToken login(@RequestBody LoginRequestBody loginRequestBody) {
        return myUserService.authenticate(loginRequestBody.getUsername(), loginRequestBody.getPassword());
    }
}

