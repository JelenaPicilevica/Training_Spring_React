package com.example.training_spring_react.controllers;

import com.example.training_spring_react.models.User;
import com.example.training_spring_react.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.List;

@CrossOrigin
@RestController
//@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    //Show all users
//    @GetMapping
//    public List<User> getUsers() {
//        return userService.findAllUsers();
//    }


    //Back-end returns data and front-end is responsible for navigation

    @GetMapping("/login")
    public Boolean loginSuccess(){
        return true;
    }

    @PostMapping("/signIn")
    public ResponseEntity loginUser (@RequestBody User user) throws URISyntaxException {
       try {
           User loggedInUser = userService.verifyUser(user);
           System.out.println(loggedInUser);
           return ResponseEntity.accepted().body(user);
       }catch (Exception e){
           System.out.println(user);
           return ResponseEntity.badRequest().body("Bad request");
       }

       //true or false - Boolean metode
    }
}
