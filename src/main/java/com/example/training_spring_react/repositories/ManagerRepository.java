//package com.example.training_spring_react.repositories;
//
//import com.example.training_spring_react.models.Client;
//import com.example.training_spring_react.models.Manager;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.stereotype.Repository;
//
//import java.util.ArrayList;
//
//@Repository
//public interface ManagerRepository extends JpaRepository<Manager, Long> {
//
//    Manager findManagerById(Long id);
//
//    @Query(value = "SELECT managers.* FROM managers INNER JOIN (SELECT manager_id, COUNT(manager_id) AS countID" +
//            " FROM clients GROUP BY manager_id HAVING COUNT(manager_id) = (SELECT MAX(countID) " +
//            "FROM(SELECT manager_id, COUNT(manager_id) AS countID FROM clients GROUP BY manager_id) AS maxValue)) AS" +
//            " tablet ON managers.id = tablet.manager_id", nativeQuery = true)
//    ArrayList<Manager> findManagersWithLargestClientNumber();
//
//
//    @Query(value = "SELECT managers.* FROM managers INNER JOIN (SELECT manager_id, COUNT(manager_id) AS countID" +
//            " FROM clients GROUP BY manager_id HAVING COUNT(manager_id) = (SELECT MIN(countID) " +
//            "FROM(SELECT manager_id, COUNT(manager_id) AS countID FROM clients GROUP BY manager_id) AS maxValue)) AS" +
//            " tablet ON managers.id = tablet.manager_id", nativeQuery = true)
//    ArrayList<Manager> findManagersWithSmallestClientNumber();
//
//
//}
