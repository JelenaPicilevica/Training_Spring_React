package com.example.training_spring_react.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "clients")
public class Client {

    @Id
    @GeneratedValue
    private Long id;

    @NotBlank(message = "Name is mandatory")
    @Size(max=55, message = "Name shouldn't be longer than 55 symbols and must contain at least 1 letter")
    private String name;

    @NotBlank(message = "Email is mandatory")
    @Size(max=50, message = "Email size should be until 50 symbols")
    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}", flags = Pattern.Flag.CASE_INSENSITIVE)
    private String email;

    @NotNull(message = "Date of birth is mandatory")
    private LocalDate dob;
    private Integer age;        //calculated automatically from dob

    //@Column(columnDefinition = "long default 0")
    private Long link;  //link to other client

    private Long linkCount;  //number of links starting from this client

//    @ManyToOne                                                          //one manager can have many clients
//    @PrimaryKeyJoinColumn(name = "manager_id", referencedColumnName = "id")
//    private Manager manager;


    @JoinColumn (name = "manager_id", insertable = false, updatable = false)
    @ManyToOne(targetEntity = Manager.class, fetch = FetchType.EAGER)
    private Manager manager;

    @Column(name = "manager_id")
    private Long managerID;

    //Additional constructor if manager is not inputted
//    public Client(String name, String email, LocalDate dob, Long link) {
//        this.name = name;
//        this.email = email;
//        this.dob = dob;
//        this.link = link;
//    }
}
