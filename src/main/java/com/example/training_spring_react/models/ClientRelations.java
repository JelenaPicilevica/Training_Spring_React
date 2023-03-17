package com.example.training_spring_react.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data

@Entity
@Table(name = "client_relations")
public class ClientRelations {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @OneToOne(targetEntity = Client.class, fetch = FetchType.EAGER)
//    @JoinColumn(name = "child_id", insertable = false, updatable = false)
//    private Client client;

    @Column(name = "child_id")
    private Long childID;


    //TEST HERE

    //In these table can be many parents but only one such client will be find in table clients
    @ManyToOne(targetEntity = Client.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_id", insertable = false, updatable = false)
    private Client client;

    @Column(name = "parent_id")
    private Long parentID;


}
