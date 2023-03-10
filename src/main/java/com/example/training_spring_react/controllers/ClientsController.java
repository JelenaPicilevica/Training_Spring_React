package com.example.training_spring_react.controllers;

import com.example.training_spring_react.models.Client;
import com.example.training_spring_react.repositories.ClientRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/clients")
public class ClientsController {

    //Getting ClientRepository object and adding in constructor
    private final ClientRepository clientRepository;

    public ClientsController(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }


    //Client list for default path '/clients' (in Controller)
    @GetMapping
    public List<Client> getClients() {
        return clientRepository.findAll();
    }

    //Youngest client list,  path '/clients/youngest'
    @GetMapping("/youngest")
    public List<Client> getYoungestClients() {
        return clientRepository.findYoungestClients();
    }


    //Linked client list, path '/clients/linked'
    @GetMapping("/linked")
    public List<Client> getLinkedClients() {
        return clientRepository.findLinkedClients();
    }


    //Getting client by id from URI
    @GetMapping("/{id}")
    public Client getClient(@PathVariable Long id) {
        return clientRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    //Create and save client ( ResponseEntity => HTTP response (status code, header, body)
    //@ Valid - when the target argument fails to pass the validation, Spring Boot throws a
    //MethodArgumentNotValidException exception.

    //@RequestBody => JSON data to JAVA object
    @PostMapping
    public ResponseEntity createClient(@Valid @RequestBody Client client) throws URISyntaxException {
        //Setting age automatically
        client.setAge(Period.between(client.getDob(), LocalDate.now()).getYears());

        //VALIDATION OF LINK

        //Reference to other client entered by user
        long referenceToOtherClient = client.getLink();

        //1. If client with id defined as reference exists or link is 0 => counting number of links and set to client
        if(clientRepository.findClientById(referenceToOtherClient) != null){

            //Calculating number of links with function and set this number to client
            long numberOfLinks = countLinks(client);
            client.setLinkCount(numberOfLinks);


        //2. If client with id defined as reference NOT exists => we set link as 0 and accordingly link count as 0
        }else{
            client.setLink(0L);
            client.setLinkCount(0L);  //if link was set incorrectly => total link count = 0
            //OLD: return ResponseEntity.badRequest().body("This client link does NOT exists: " + referenceToOtherClient);
        }

        //Saving client and returning ResponseEntity
        Client savedClient = clientRepository.save(client);
        return ResponseEntity.created(new URI("/clients/" + savedClient.getId())).body(savedClient);
    }


    /*
    The @ExceptionHandler annotation allows us to handle specified types of exceptions through
     one single method, we can use it for processing the validation errors

     We specified the MethodArgumentNotValidException exception as the exception to be handled.
     Consequently, Spring Boot will call this method when the specified Client object is invalid.

     The method stores the name and post-validation error message of each invalid field in a Map.
     Next it sends the Map back to the client as a JSON representation for further processing.
     */

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }


    //Update client (looking for client by id from URI, setting new values, saving again)
    @PutMapping("/{id}")
    public ResponseEntity updateClient(@PathVariable Long id, @RequestBody Client client) {
        Client currentClient = clientRepository.findById(id).orElseThrow(RuntimeException::new);

        currentClient.setName(client.getName());
        currentClient.setEmail(client.getEmail());
        currentClient.setDob(client.getDob());
        currentClient.setAge(Period.between(currentClient.getDob(), LocalDate.now()).getYears());

        //VALIDATION OF LINK

        //Reference to other client entered by user
        long referenceToOtherClient = client.getLink();


        //If client with id defined as reference exists or value is 0 => we set this link to editable client as reference
        if(clientRepository.findClientById(referenceToOtherClient) != null || referenceToOtherClient == 0){
            currentClient.setLink(referenceToOtherClient);

            //Calculating number of links with function and set this number to client
            long numberOfLinks = countLinks(currentClient);
            currentClient.setLinkCount(numberOfLinks);

            clientRepository.save(currentClient);
            return ResponseEntity.ok(currentClient);

            //If client with id defined as reference NOT exists => we don't client
        }else{
            return ResponseEntity.badRequest().body("This client link does NOT exists: " + referenceToOtherClient);
        }

    }


    //Deleting client by ID from URI
    @DeleteMapping("/{id}")
    public ResponseEntity deleteClient(@PathVariable Long id) {
        clientRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }


    //Method for link counting
    public long countLinks (Client client){
        //Defining variables
        long countingNumOfLinks = 0;      //
        long userId = client.getLink();  //id of client from the link (link frm client we create)

        //Counting through while cycle=> if next link found, we count +1 and take this link as new user id

        while (userId != 0L){
            countingNumOfLinks +=1;
            long foundLink = clientRepository.findLinkByClientId(userId);  //found link => id of other client
            userId = foundLink;                                 //new user id = link that is id of other client
            System.out.println("Counting links: " + countingNumOfLinks);
            System.out.println("Found next link: " + userId);
        }

        System.out.println("Link calculation stopped");
        return countingNumOfLinks;
    }



}
