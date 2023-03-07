package com.example.training_spring_react;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;

public interface ClientRepository extends JpaRepository<Client, Long> {

    @Query(value = "SELECT * FROM clients WHERE age = (SELECT MIN(age)FROM clients)", nativeQuery = true)
    ArrayList<Client> findYoungestClients();

}