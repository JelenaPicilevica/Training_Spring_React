package com.example.training_spring_react.repositories;

import com.example.training_spring_react.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;

public interface ClientRepository extends JpaRepository<Client, Long> {

    @Query(value = "SELECT * FROM clients WHERE age = (SELECT MIN(age)FROM clients)", nativeQuery = true)
    ArrayList<Client> findYoungestClients();

    //For Linked Clients page
    @Query(value = "SELECT * FROM clients WHERE link != '0'", nativeQuery = true)
    ArrayList<Client> findLinkedClients();

    Client findClientById(Long id);


    @Query(value = "SELECT link FROM clients WHERE id= :id", nativeQuery = true)
    Long findLinkByClientId(@Param("id") long id);


}