package com.example.training_spring_react.services;

import com.example.training_spring_react.repositories.ClientRelationsRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ClientRelationsService {

    private final ClientRelationsRepository clientRelationsRepository;

    public ClientRelationsService(ClientRelationsRepository clientRelationsRepository) {
        this.clientRelationsRepository = clientRelationsRepository;
    }


    //COUNTING LEVELS UNDER ONE CLIENT (levels of childs)
    public Long findNumOfLevels(long parentID){

        //0. EMPTY LISTS FOR DIRECT CHILDS (childsOfThisClient) AND resultCount for counting
        List<Long> childsOfThisClient = new ArrayList<>();
        long resultCount = 0;

        //1. IF DIRECT CHILDS EXIST => PUTTING THEM IN THE LIST 'childsOfThisClient' and counting +1
           //IF NOT EXIST => return resultCount ('0')

        childsOfThisClient = clientRelationsRepository.findChildsOfClientParent(parentID);
        childsOfThisClient.removeIf(Objects::isNull);

        if(childsOfThisClient.isEmpty()){
            return resultCount;
        }else{
            resultCount += 1;
        }

        //2. LOOKING FOR CHILDS OF OUR DIRECT CHILDS

        //List for new found childs, will add there all childs of childs
        List<Long> newFoundChilds = new ArrayList<>();

        //Checking each child of client for next childs and putting them in the list 'newFoundChilds'
        for (int i =0; i<childsOfThisClient.size(); i++){

            newFoundChilds.addAll(clientRelationsRepository.findChildsOfClientParent(childsOfThisClient.get(i)));
            // !!!!! We need to exclude null values (for direct childs who don't have next childs)
            newFoundChilds.removeIf(Objects::isNull);

            //3. IF CHILDS OF CHILDS WERE NOT FOUND => Return result count (= 1 as only direct childs were found)
            if (newFoundChilds.isEmpty()){
                return resultCount; //('1' as only direct childs were found)
            }else {
                // 4. ADDING 1 MORE LEVEL FOUND ('2') 1st level => childs  2nd level => childs of childs
                resultCount += 1;
            }

            //4. FINDING CHILDS FOR PREVIOUSLY FOUND CHILDS ('ņewFoundChilds')
            // AND UPDATING 'newFoundChilds' LIST WITH NEW DATA

            // Client => Direct childs => Childs of direct => we are on this level
            while (!newFoundChilds.isEmpty()){
                List<Long> newListOfChilds = new ArrayList<>();

                for(int j =0; j<newFoundChilds.size(); j++){
                    newListOfChilds.addAll(clientRelationsRepository.findChildsOfClientParent(newFoundChilds.get(j)));
                }

                //Updating 'NewFoundChilds' list with newly found data, taking off null values
                newFoundChilds.clear();
                newFoundChilds.addAll(newListOfChilds);
                newFoundChilds.removeIf(Objects::isNull);

                //Counting next level
                if(newFoundChilds != null){
                    resultCount += 1;
                }
            }
        }
        return resultCount;
    }


    //COUNTING ALL CHILDS AND SUB-CHILDS RELATED TO ONE CLIENT
    public Long findChildrenList(long parentID){

        //0. EMPTY LISTS FOR DIRECT CHILDS (childsOfThisClient) AND FOR ALL CHILDS (resultList)
        List<Long> childsOfThisClient = new ArrayList<>();
        List<Long> resultList = new ArrayList<>();


        //1. IF DIRECT CHILDS EXIST => PUTTING THEM IN THE LIST 'childsOfThisClient'

        childsOfThisClient = clientRelationsRepository.findChildsOfClientParent(parentID);
        childsOfThisClient.removeIf(Objects::isNull);

        if(childsOfThisClient != null){
            resultList.addAll(childsOfThisClient); //Direct childs now are in the result
        }else{
            return 0L;
        }

        //2. LOOKING FOR CHILDS OF OUR DIRECT CHILDS

        //List for new found childs, will add there all childs of childs
        List<Long> newFoundChilds = new ArrayList<>();


        //Checking each child of client for next childs and putting them in the list 'newFoundChilds'
        for (int i =0; i<childsOfThisClient.size(); i++){

            newFoundChilds.addAll(clientRelationsRepository.findChildsOfClientParent(childsOfThisClient.get(i)));
            // !!!!! We need to exclude null values (for direct childs who don't have next childs)
            newFoundChilds.removeIf(Objects::isNull);


            //3. ADDING ALL FOUND CHILDS OF DIRECT CHILDS TO THE LIST (already included direct childs)
            resultList.addAll(newFoundChilds);

            if (newFoundChilds.isEmpty()){
                return (long) resultList.size();
            }

            //4. FINDING CHILDS FOR PREVIOUSLY FOUND CHILDS ('ņewFoundChilds')
            // AND UPDATING 'newFoundChilds' LIST WITH NEW DATA

            // Client => Direct childs => Childs of direct => we are on this level
            while (!newFoundChilds.isEmpty()){
                List<Long> newListOfChilds = new ArrayList<>();

                for(int j =0; j<newFoundChilds.size(); j++){
                    newListOfChilds.addAll(clientRelationsRepository.findChildsOfClientParent(newFoundChilds.get(j)));
                }

                //Updating 'NewFoundChilds' list with newly found data, taking off null values
                newFoundChilds.clear();
                newFoundChilds.addAll(newListOfChilds);
                newFoundChilds.removeIf(Objects::isNull);

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

    public void deleteRelationship(long clientID){
        clientRelationsRepository.deleteByChildID(clientID);
    }
}
