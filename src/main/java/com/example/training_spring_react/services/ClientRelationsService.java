package com.example.training_spring_react.services;

import com.example.training_spring_react.repositories.ClientRelationsRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClientRelationsService {

    private final ClientRelationsRepository clientRelationsRepository;

    public ClientRelationsService(ClientRelationsRepository clientRelationsRepository) {
        this.clientRelationsRepository = clientRelationsRepository;
    }


    //Finding childs related to one client
    public Long findChildrenList(long parentID){

        //Direct childs putin the list 'childsOfThisClient'
        List<Long> childsOfThisClient = clientRelationsRepository.findChildsOfClientParent(parentID);
        List<Long> resultList = new ArrayList<>();

        //Looking childs for direct childs
        for (int i =0; i<childsOfThisClient.size(); i++){

            //List for new found childs, adding there all childs of childs
            List<Long> newFoundChilds = new ArrayList<>();
            newFoundChilds.addAll(clientRelationsRepository.findChildsOfClientParent(newFoundChilds.get(i)));

            //ALL CHILDS (including direct)
            resultList.addAll(newFoundChilds);

            while (!newFoundChilds.isEmpty()){
                for(int j =0; j<newFoundChilds.size(); j++){
                    newFoundChilds = clientRelationsRepository.findChildsOfClientParent(newFoundChilds.get(j));
                    resultList.addAll(newFoundChilds);
                }
            }
        }
        return (long)resultList.size();
    }

    public boolean setRelationship (long clientID, long parentID){
        this.clientRelationsRepository.insertRelationsData(clientID, parentID);
        return true;
    }

    public boolean updateRelationship (long clientID, long parentID){
        this.clientRelationsRepository.updateRelationsData(parentID,clientID);
        return true;
    }
}
