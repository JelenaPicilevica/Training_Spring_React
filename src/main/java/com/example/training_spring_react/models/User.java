package com.example.training_spring_react.models;

//implements UserDetails interface (Spring security) => data from this class will be used for authorization

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="users")
@Data
//public class User implements UserDetails {
    public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "email", unique = true)                  //email will be our username
    private String email;
    @Column(name = "numberPhone", unique = true)
    private String numberPhone;
    @Column(name = "name")
    private String name;
    @Column(name = "active")
    private boolean active;
    //    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    @JoinColumn(name = "image_id")
//    private Image avatar;
    @Column(name = "password", length = 1000)             //big number of symbols due to hashing
    private String password;

    private LocalDateTime dateOfCreated;

    //Here we initialize date of creation
    @PrePersist
    private void init(){
        dateOfCreated = LocalDateTime.now();
    }

}
