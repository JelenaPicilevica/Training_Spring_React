package com.example.training_spring_react.services;

import com.example.training_spring_react.repositories.ClientRelationsRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClientRelationsService {

    private final ClientRelationsRepository clientRelationsRepository;

    public ClientRelationsService(ClientRelationsRepository clientRelationsRepository) {
        this.clientRelationsRepository = clientRelationsRepository;
    }


    //COUNTING ALL CHILDS AND SUB-CHILDS RELATED TO ONE CLIENT
    public Long findChildrenList(long parentID){

        //0. LIST FOR DIRECT CHILDS (childsOfThisClient) AND FOR ALL CHILDS (resultList)
        List<Long> childsOfThisClient = new ArrayList<>();
        List<Long> resultList = new ArrayList<>();


        //1. IF DIRECT CHILDS EXIST => PUTTING THEM IN THE LIST 'childsOfThisClient'

        if(clientRelationsRepository.findChildsOfClientParent(parentID) != null){

            childsOfThisClient = clientRelationsRepository.findChildsOfClientParent(parentID);
            resultList.addAll(childsOfThisClient); //Direct childs now are in the result
        }else{
            return 0L;
        }

        //3. LOOKING FOR CHILDS OF OUR DIRECT CHILDS

        //List for new found childs, will add there all childs of childs
        List<Long> newFoundChilds = new ArrayList<>();

        //Checking each child of client for next childs and putting them in the list 'newFoundChilds'
        for (int i =0; i<childsOfThisClient.size(); i++){
            newFoundChilds.addAll(clientRelationsRepository.findChildsOfClientParent(newFoundChilds.get(i)));

            //4. ADDING ALL FOUND CHILDS OF DIRECT CHILDS TO THE LIST (already included direct childs)
            resultList.addAll(newFoundChilds);


            //5. FINDING CHILDS FOR PREVIOUSLY FOUND CHILDS ('ņewFoundChilds')
            // AND UPDATING 'newFoundChilds' LIST WITH NEW DATA

            // Client => Direct childs => Childs of direct => we are on this level
            while (!newFoundChilds.isEmpty()){
                List<Long> newListOfChilds = new ArrayList<>();

                for(int j =0; j<newFoundChilds.size(); j++){
                    newListOfChilds.addAll(clientRelationsRepository.findChildsOfClientParent(newFoundChilds.get(j)));
                }

                //Updating 'NewFoundChilds' list with newly found data
                newFoundChilds.clear();
                newFoundChilds.addAll(newListOfChilds);
                //Adding newly found data to the result
                resultList.addAll(newFoundChilds);
            }
        }
        return (long)resultList.size();
    }

    public boolean setRelationship (long clientID, long parentID){
        this.clientRelationsRepository.insertRelationsData(clientID, parentID);
        return true;
    }

    public void updateRelationship (long clientID, long parentID){
        this.clientRelationsRepository.updateRelationsData(parentID,clientID);
    }
}
