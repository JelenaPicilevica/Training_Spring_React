//package com.example.training_spring_react.services;
//
//import com.example.training_spring_react.models.User;
//import com.example.training_spring_react.repositories.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class CustomUserDetailsService implements UserDetailsService {
//    private final UserRepository userRepository;
//
//
//    //shows how to load users, we will use it in SecurityConfig
//
////    @Override
////    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
////        return userRepository.findByEmail(email);
////    }
//
//
//    @Override
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//
//        User userFoundInDB = userRepository.findByEmail(email);
//
//        if(userFoundInDB == null){
//            System.out.println("User NOT found");
//            System.out.println("Email: " + email);
//            throw new UsernameNotFoundException("User not authorized.");
//        }
//        System.out.println("User was found");
//        return userFoundInDB;
//    }
//}
