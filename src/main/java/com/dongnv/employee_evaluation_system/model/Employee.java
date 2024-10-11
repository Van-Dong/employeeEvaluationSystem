package com.dongnv.employee_evaluation_system.model;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
@Entity
@Table(
        name = "employee",
        indexes = {@Index(name = "idx_employee_full_name", columnList = "full_name")})
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotBlank
    @NotNull
    String fullName;

    @NotNull
    Boolean isMale;

    String imageUrl;
    LocalDate dob;
    Double salary;
    Byte level;
    String email;
    String phone;
    String note;

    @ManyToOne
    Department department;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "employee_id")
    List<Evaluation> evaluations;
}
