package com.example.training_spring_react.controllers;

import com.example.training_spring_react.models.Client;
import com.example.training_spring_react.repositories.ClientRelationsRepository;
import com.example.training_spring_react.repositories.ClientRepository;
import com.example.training_spring_react.services.ClientRelationsService;
import org.springframework.boot.autoconfigure.mail.MailProperties;
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
import java.util.*;

@CrossOrigin
@RestController
@RequestMapping("/clients")
public class ClientsController {

    //Getting ClientRepository object and adding in constructor
    private final ClientRepository clientRepository;
    private final ClientRelationsService clientRelationsService;

    //    private final ManagerService managerService;

    public ClientsController(ClientRepository clientRepository, ClientRelationsRepository clientRelationsRepository, ClientRelationsService clientRelationsService) {
        this.clientRepository = clientRepository;
        this.clientRelationsService = clientRelationsService;
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


    //Finding a CEO by levels below, path '/clients/CEObyLevels'
    @GetMapping("/CEObyLevels")
    public List<Client> getCEOByLevelsBelow() {

        //1. FINDING ALL CLIENTS, PUTTING THEM INTO THE LIST
        List<Client> allClientList = clientRepository.findAllClients();

        //2. CREATING A MAP WHERE CLIENT ID AND CALCULATED LEVELS WILL BE STORED
        Map<Long, Long> clientIdAndLevelCount = new HashMap<>();

        //3. CALCULATING LEVELS BELOW FOR EACH CLIENT FROM THE LIST

        long levelCount;

        for(int i = 0; i< allClientList.size(); i++){

            //3.1. currentClient => client found in DB by id
            Client currentClient = clientRepository.findById(allClientList.get(i).getId()).orElseThrow(RuntimeException::new);

            //3.2. Calculating levels for currentClient and setting this number as levelsBelow value

            if(currentClient.getId() == 0){
                levelCount = 0;
            }else{
                levelCount = clientRelationsService.findNumOfLevels(currentClient.getId());
            }

            //4. PUTTING CLIENT ID AND LEVEL COUNT INTO MAP
            clientIdAndLevelCount.put(currentClient.getId(), levelCount);
        }

        //5. WHEN LEVELS FOR ALL CLIENTS CALCULATED => WE SHOULD FIND CLIENT IDs THAT ARE CEO

        //5.1 Looking for MAX calculated level value in Map:
        long maxValueInMap=(Collections.max(clientIdAndLevelCount.values()));

        //5.2 Creating List where will be stored client IDs containing MAX found level value:
        List <Long> idListOfCEO = new ArrayList<>();

        //5.3 Looking for keys (=client IDs) containing MAX value and putting them into the list:
        for (Map.Entry<Long, Long> entry : clientIdAndLevelCount.entrySet()) {  // Iterate through hashmap
            if (entry.getValue()==maxValueInMap) {
                idListOfCEO.add(entry.getKey());
            }
        }

        //6. WHEN CLIENTS ID WITH MAX LEVEL COUNT PUT IN THE LIST => FINDING A CEO
        ArrayList<Client> foundCEObyLevels = clientRepository.findClientByIdIn(idListOfCEO);

        return foundCEObyLevels;
    }


    //Finding a CEO by child count, path '/clients/CEO'
//    @GetMapping("/CEO")
//    public List<Client> getCEO() {
//
//        //1. FINDING ALL CLIENTS, PUTTING THEM INTO THE LIST
//        List<Client> allClientList = clientRepository.findAllClients();
//
//        //2. CALCULATING CHILDS FOR EACH CLIENT FROM THE LIST
//        for(int i = 0; i< allClientList.size(); i++){
//
//            //2.1. currentClient => client found in DB by id
//            Client currentClient = clientRepository.findById(allClientList.get(i).getId()).orElseThrow(RuntimeException::new);
//
//            //2.2. Calculating childs for currentClient and setting this number as childCount value
//            long childNum = clientRelationsService.findChildrenList(currentClient.getId());
//            currentClient.setChildCount(childNum);
//
//            //2.3. Saving updated client
//            clientRepository.save(currentClient);
//        }
//
//        //3. WHEN CHILDS FOR ALL CLIENTS CALCULATED => WE SHOULD FIND CEO
//        ArrayList<Client> foundCEO = clientRepository.findCEO();
//
//        return foundCEO;
//    }


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

        // 1. SETTING AGE AUTOMATICALLY
        client.setAge(Period.between(client.getDob(), LocalDate.now()).getYears());

        /* 2. VALIDATION OF LINK - Checking if Link field is empty
             - if yes - we set up link and link count as 0
             - if not - checking in DB for linked user => if exists - put entered data as link,
                                                          if not exists - put 0 values */
//        linkCheckAndSetUp(client);

         /* 3. VALIDATION OF PARENT ID - Checking if parent field is empty
             - if yes - we set up parent id as 0
             - if not - checking in DB for parent id => if exists - put entered data as parent id,
                                                       if not exists - put 0 value as parent id */
        parentIdCheck(client);

        //VALIDATION OF MANAGER ID

        //If manager with such id found (true) we set it to client
        //If manager with such id NOT found (false) we set manager id as '0'

//        if (checkManagerExistenceById(client.getManagerID())){
//            client.setManagerID(client.getManagerID());
//        }else{
//            client.setManagerID(3L);
//        }

        // 4. SAVING CLIENT
        Client savedClient = clientRepository.save(client);

        //4. SAVING DATA TO CLIENT_RELATIONS TABLE
        clientRelationsService.setRelationship(savedClient.getId(), savedClient.getParentID());

       //FINDING SAVED CLIENT TO COUNT CHILDS (as now we have id)
//        Client savedClientUpdate = clientRepository.findById(savedClient.getId()).orElseThrow(RuntimeException::new);

        //COUNTING CHILDS
//        long numOfChilds = clientRelationsService.findChildrenList(savedClientUpdate.getId());
//        savedClientUpdate.setChildCount(numOfChilds);

        //SAVING AGAIN WITH COUNTED CHILDS
//        clientRepository.save(savedClientUpdate);

        //RETURNING RESPONSE ENTITY
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
//        currentClient.setManagerID(client.getManagerID());
        currentClient.setAge(Period.between(currentClient.getDob(), LocalDate.now()).getYears());

       /* VALIDATION OF LINK - Checking if Link field is empty
             - if yes - we set up link and link count as 0
             - if not - checking in DB for linked user => if exists - put entered data as link,
                                                          if not exists - put 0 values */
//        linkCheckAndSetUp(client);
//        currentClient.setLink(client.getLink());
//        currentClient.setLinkCount(client.getLinkCount());

        //VALIDATION OF MANAGER ID

        //If manager with such id found (true) we set it to client
        //If manager with such id NOT found (false) we don't save client update

//        if (checkManagerExistenceById(client.getManagerID())){            //method takes entered manager id for this client
//            currentClient.setManagerID(client.getManagerID());
//        }else{
//            return ResponseEntity.badRequest().body("Manager with this id does NOT exists: " + client.getManagerID());
//        }


         /* VALIDATION OF PARENT ID - Checking if parent field is empty
             - if yes - we set up parent id as 0
             - if not - checking in DB for parent id => if exists - put entered data as parent id,
                                                       if not exists - put 0 value as parent id */
        parentIdCheck(client);
        currentClient.setParentID(client.getParentID());
        clientRelationsService.updateRelationship(currentClient.getId(), currentClient.getParentID());

        //COUNTING CHILDS
//        long numOfChilds = clientRelationsService.findChildrenList(currentClient.getId());
//        System.out.println("Number of childs: " + numOfChilds);
//        currentClient.setChildCount(numOfChilds);

        //AFTER ALL CHECKINGS SAVE DATA

        clientRepository.save(currentClient);
        return ResponseEntity.ok(currentClient);
    }


    //Deleting client by ID from URI
    @DeleteMapping("/{id}")
    public ResponseEntity deleteClient(@PathVariable Long id) {
        clientRepository.deleteById(id);
        clientRelationsService.deleteRelationship(id);
        return ResponseEntity.ok().build();
    }


    //Method for link counting
//    public long countLinks (Client client){
//        //Defining variables
//        long countingNumOfLinks = 0;      //
//        long userId = client.getLink();  //id of client from the link (link frm client we create)
//
//        //Counting through while cycle=> if next link found, we count +1 and take this link as new user id
//
//        while (userId != 0L){
//            countingNumOfLinks +=1;
//            long foundLink = clientRepository.findLinkByClientId(userId);  //found link => id of other client
//            userId = foundLink;                                 //new user id = link that is id of other client
//            System.out.println("Counting links: " + countingNumOfLinks);
//            System.out.println("Found next link: " + userId);
//        }
//
//        System.out.println("Link calculation stopped");
//        return countingNumOfLinks;
//    }
//
//    public void linkCheckAndSetUp (Client client) {
//
//        if (client.getLink() == null) {
//            client.setLink(0L);
//            client.setLinkCount(0L);  //if link was set incorrectly => total link count = 0
//        } else {
//            //Reference to other client entered by user
//            long referenceToOtherClient = client.getLink();
//
//            //1. If client with id defined as reference exists or link is 0 => counting number of links and set to client
//            if (clientRepository.findClientById(referenceToOtherClient) != null) {
//
//                //Calculating number of links with function and set this number to client
//                long numberOfLinks = countLinks(client);
//                client.setLinkCount(numberOfLinks);
//
//                //2. If client with id defined as reference NOT exists => we set link as 0 and accordingly link count as 0
//            } else {
//                client.setLink(0L);
//                client.setLinkCount(0L);  //if link was set incorrectly => total link count = 0
//            }
//        }
//
//    }

        public void parentIdCheck (Client client){

            if(client.getParentID() == null){
                client.setParentID(0L);
            }else{
                long parentIdForChecking = client.getParentID();

                if(clientRepository.findClientById(parentIdForChecking) != null){
                    client.setParentID(parentIdForChecking);
                }else{
                    client.setParentID(0L);
                }
            }
        }

//    public Boolean checkManagerExistenceById (Long managerId){
//
//        if (managerService.findManagerById(managerId) != null){
//            return true;
//        }else {
//            return false;
//        }
//    }


}

