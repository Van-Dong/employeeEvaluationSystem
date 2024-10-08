package com.dongnv.employee_evaluation_system.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

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

//    public Integer getCountEmployee() {
//        return this.employees.size();
//    }
}
