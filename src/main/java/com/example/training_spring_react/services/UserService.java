package com.example.training_spring_react.services;

import com.example.training_spring_react.models.enums.Role;
import com.example.training_spring_react.models.User;
import com.example.training_spring_react.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository; //we used @RequiredArgsConstructor
//    private final PasswordEncoder passwordEncoder;  //we used @RequiredArgsConstructor (from Spring Security)


    //Create User - boolean method
    //If user was found (result != null) => false (new user will not be created)
    //If user was not found = true (new user will be created)

    public boolean createUser (User user){
        String email = user.getEmail();
        if (userRepository.findByEmail(email) != null) return false; //user with such email was found
        user.setActive(true);
//        user.setPassword(passwordEncoder.encode(user.getPassword())); //hashing password
        user.getRoles().add(Role.ROLE_USER);
        log.info("Saving new User with email: {}", email);
        userRepository.save(user);
        return true;
    }

//    public List<User> findAllUsers(){
//       return this.userRepository.findAll();
//    }
//
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