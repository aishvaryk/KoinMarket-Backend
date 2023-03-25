package com.koinmarket.app.requestBodies;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RegisterRequestBody {
    private String username;
    private String emailAddress;
    private String password;

}
