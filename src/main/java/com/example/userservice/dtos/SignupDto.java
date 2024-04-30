package com.example.userservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupDto {
    private String name;
    private String email;
    private String password;
}
