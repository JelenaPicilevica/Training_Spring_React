package com.example.training_spring_react.repositories;

import com.example.training_spring_react.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    //For Youngest Clients page
    @Query(value = "SELECT * FROM clients WHERE age = (SELECT MIN(age)FROM clients)", nativeQuery = true)
    ArrayList<Client> findYoungestClients();

    //For Linked Clients page
    @Query(value = "SELECT * FROM clients WHERE link != '0'", nativeQuery = true)
    ArrayList<Client> findLinkedClients();


    //For finding a CEO - we look for a client with bigest number of childs
    @Query(value = "SELECT * FROM clients WHERE child_count = (SELECT MAX(child_count) FROM clients WHERE id != 0)", nativeQuery = true)
    ArrayList<Client> findCEO();

    //For finding a CEO by levels - we look for a client with bigest number of levels below
    @Query(value = "SELECT * FROM clients WHERE levels_below = (SELECT MAX(levels_below) FROM clients WHERE id != 0)", nativeQuery = true)
    ArrayList<Client> findCEObyLevels();

    @Query(value = "SELECT * FROM clients", nativeQuery = true)
    ArrayList<Client> findAllClients();

    //Searching client by ID
    Client findClientById(Long id);

    //Finding a link related to client for link count
    @Query(value = "SELECT link FROM clients WHERE id= :id", nativeQuery = true)
    Long findLinkByClientId(@Param("id") long id);


}