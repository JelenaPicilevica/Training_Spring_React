package com.example.training_spring_react.repositories;

import com.example.training_spring_react.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    //As our users login with e-mail we need to find them in DB by e-mail
    User findByEmail(String email);


    User findByEmailAndPassword(String email, String password);  //for login purposes


}
