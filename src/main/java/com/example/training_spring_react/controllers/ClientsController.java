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

        //If client with id defined as reference exists => we save client
        if(clientRepository.findClientById(referenceToOtherClient) != null){
            Client savedClient = clientRepository.save(client);
            return ResponseEntity.created(new URI("/clients/" + savedClient.getId())).body(savedClient);

        //If client with id defined as reference NOT exists => we don't client
        }else{
            return ResponseEntity.badRequest().body("This client link does NOT exists: " + referenceToOtherClient);
        }
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

        //If client with id defined as reference exists => we save client
        if(clientRepository.findClientById(referenceToOtherClient) != null){
            //System.out.println(clientRepository.findById(referenceToOtherClient));
            currentClient.setLink(referenceToOtherClient);
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

}