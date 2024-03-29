//package com.example.training_spring_react.controllers;
//
//import com.example.training_spring_react.models.Manager;
//import com.example.training_spring_react.services.ManagerService;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//
//@RestController
//public class ManagerController {
//
//    private ManagerService managerService;
//
//    public ManagerController(ManagerService managerService) {
//        this.managerService = managerService;
//    }
//
//    //Show all users
//  @GetMapping("/managers")
//    public List<Manager> getManagers() {
//        return managerService.findAllManagers();
//    }
//
//    @GetMapping("/managers/top")
//    public List<Manager> getTopManagers() {
//        return managerService.findTopManagers();
//    }
//
//    @GetMapping("/managers/weakest")
//    public List<Manager> getWeakestManagers() {
//        return managerService.findWeakestManagers();
//    }
//
//}
