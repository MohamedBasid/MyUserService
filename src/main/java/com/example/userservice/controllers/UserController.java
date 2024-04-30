package com.example.userservice.controllers;

import com.example.userservice.dtos.LoginDto;
import com.example.userservice.dtos.SignupDto;
import com.example.userservice.models.Token;
import com.example.userservice.models.User;
import com.example.userservice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/hi")
    public String hi(){
        return "Hi";
    }

    @PostMapping("/signup")
    public ResponseEntity<User> signup(@RequestBody SignupDto signupDto){
        System.out.println("Hiii");
        User user = userService.signUp(signupDto.getEmail(), signupDto.getPassword(), signupDto.getName());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<Token> login(@RequestBody LoginDto loginDto){
        Token token = userService.login(loginDto.getEmail(), loginDto.getPassword());
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @PostMapping("/logout/{token}")
    public String logout(@PathVariable("token") String token){
        userService.logout(token);
        return "Logged out successfully";
    }

    @PostMapping("/validateToken/{token}")
    public boolean validateToken(@PathVariable("token") String tokenValue){
        return userService.validateToken(tokenValue);
    }
}
