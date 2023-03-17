package com.example.training_spring_react.repositories;

import com.example.training_spring_react.models.ClientRelations;
import com.example.training_spring_react.models.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface ClientRelationsRepository extends JpaRepository <ClientRelations, Long> {

    @Query(value = "INSERT INTO client_relations (child_id, parent_id) VALUES (?1,?2)", nativeQuery = true)
    void insertRelationsData(long clientID, long parentID);

    @Query(value = "UPDATE client_relations SET parent_id = ?1 WHERE child_id = ?2", nativeQuery = true)
    void updateRelationsData(long parentID, long childID);


    //finding childs of parent client
    @Query(value = "SELECT child_id FROM client_relations WHERE parent_id = ?1", nativeQuery = true)
    List<Long> findChildsOfClientParent(long parentID);
}
