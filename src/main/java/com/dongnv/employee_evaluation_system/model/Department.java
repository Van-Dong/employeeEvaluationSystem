package com.dongnv.employee_evaluation_system.model;

import java.util.List;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(unique = true)
    String code;

    @Column(unique = true)
    String name;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    List<Employee> employees;

    @Transient
    Integer countEmployee;
}
