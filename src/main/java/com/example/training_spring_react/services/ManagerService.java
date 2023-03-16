package com.example.training_spring_react.services;

import com.example.training_spring_react.models.Manager;
import com.example.training_spring_react.models.User;
import com.example.training_spring_react.repositories.ManagerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManagerService {

    private final ManagerRepository managerRepository;

    public ManagerService(ManagerRepository managerRepository) {
        this.managerRepository = managerRepository;
    }


    //Method to find all managers, put them in the list and return
    public List <Manager> findAllManagers(){
        return this.managerRepository.findAll();
    }

    public List <Manager> findTopManagers(){
        return this.managerRepository.findManagersWithLargestClientNumber();
    }

    public List <Manager> findWeakestManagers(){
        return this.managerRepository.findManagersWithSmallestClientNumber();
    }

    public Manager findManagerById(Long managerId){
       return managerRepository.findManagerById(managerId);
    }

}
