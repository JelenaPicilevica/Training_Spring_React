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

        //1. DIRECT CHILDS PUTTING IN THE LIST 'childsOfThisClient'
        List<Long> childsOfThisClient = clientRelationsRepository.findChildsOfClientParent(parentID);

        //2. CREATING RESULT LIST WHERE ALL FOUND CHILD DATA WILL BE STORED AND
        //   ADDING THERE DIRECT CHILDREN
        List<Long> resultList = new ArrayList<>();
        resultList.addAll(childsOfThisClient); //Direct childs now are in the result

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

    public boolean updateRelationship (long clientID, long parentID){
        this.clientRelationsRepository.updateRelationsData(parentID,clientID);
        return true;
    }
}
