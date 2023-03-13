package com.example.training_spring_react.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
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

    private Long link;  //link uz citu klientu

}
