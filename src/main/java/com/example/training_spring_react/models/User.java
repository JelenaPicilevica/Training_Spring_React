package com.example.training_spring_react.models;

//implements UserDetails interface (Spring security) => data from this class will be used for authorization

import com.example.training_spring_react.models.enums.Role;
import lombok.Data;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

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

    //Active user => can login, logout, see info.
    //Non-active user => could be user that not approved registration via phone/email, banned user...
    @Column(name = "active")
    private boolean active;
    //    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    @JoinColumn(name = "image_id")
//    private Image avatar;
    @Column(name = "password", length = 1000)             //big number of symbols due to hashing
    private String password;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();                  //Set of roles

    private LocalDateTime dateOfCreated;

    //Here we initialize date of creation
    @PrePersist
    private void init(){
        dateOfCreated = LocalDateTime.now();
    }


    //SPRING SECURITY:

    //Implementation of methods from UserDetails interface (all methods below):
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return roles;
//    }
//
//    @Override
//    public String getUsername() {      //we login with email that is instead of username
//        return email;
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {         //activity type (could be set as true if no additional functions planed)
//        return active;                   //as we are planing to ban users we set value as our active field
//    }
}
