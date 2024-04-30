package com.example.userservice.services;

import com.example.userservice.models.Token;
import com.example.userservice.models.User;
import com.example.userservice.repositories.TokensRepository;
import com.example.userservice.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    UsersRepository repo;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private TokensRepository tokensRepository;

    public User signUp(String email, String password, String name){
        Optional<User> userOptional = repo.findByEmail(email);
        if(userOptional.isPresent()) {
            throw new RuntimeException("Email already exists");
        }
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        repo.save(user);
        return user;
    }

    public Token login(String email, String password){
        Optional<User> userOptional = repo.findByEmail(email);
        if(userOptional.isEmpty()) {
            throw  new RuntimeException("Invalid email");
        }
        User user = userOptional.get();
        if(!bCryptPasswordEncoder.matches(password, user.getPassword())){
            throw new RuntimeException("Invalid password");
        }

        Token token = new Token();
        token.setUser(user);
        token.setValue(UUID.randomUUID().toString());
        Date expiryDate = getExpiryDate();
        token.setExpireAt(expiryDate);
        token.setActive(true);
        tokensRepository.save(token);
        return token;
    }
    private Date getExpiryDate(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, 2);
        return  calendar.getTime();
    }

    public void logout(String token) {
        Optional<Token> tokenOptional = tokensRepository.findByValueAndActiveEquals(token, true);
        if(tokenOptional.isEmpty()){
            throw new RuntimeException("Invalid token");
        }
        Token validToken = tokenOptional.get();
        validToken.setActive(false);
        tokensRepository.save(validToken);
    }

    public boolean validateToken(String tokenValue) {
        Date date = new Date();
        Optional<Token> tokenOptional = tokensRepository.findByValueAndActiveEqualsAndExpireAtGreaterThanEqual(tokenValue, true, date);
        return tokenOptional.isPresent();
    }
}
