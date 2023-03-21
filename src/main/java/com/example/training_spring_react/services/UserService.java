package com.example.training_spring_react.services;

import com.example.training_spring_react.models.User;
import com.example.training_spring_react.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository; //we used @RequiredArgsConstructor


    public User verifyUser (User userLoginRequest) throws Exception {

        //Checking for a user in DB
        User foundUser = this.userRepository.findByEmailAndPassword(userLoginRequest.getEmail(), userLoginRequest.getPassword());

//        //If we don't find => exception will be thrown
        if(foundUser==null){
            throw new Exception("Username or password is incorrect");
        }
        //If we find user => we return userinfo
        return foundUser;
    }
}