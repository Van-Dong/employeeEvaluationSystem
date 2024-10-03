package com.dongnv.employee_evaluation_system.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String fullName;
    Boolean isMen;
    String profileUrl;
    LocalDate dob;
    Double salary;
    Byte level;
    String email;
    String phone;
    String note;

    @OneToMany
    List<Evaluation> evaluations;
}
