package com.dongnv.employee_evaluation_system.model;

import java.time.LocalDate;

import jakarta.persistence.*;

import org.hibernate.annotations.CreationTimestamp;

import com.dongnv.employee_evaluation_system.constant.UserRole;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "account_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(unique = true, nullable = false)
    String username;

    String password;

    @Enumerated(EnumType.STRING)
    @Builder.Default // Save name of enum in database. EnumType.ORDINAL will save index
    UserRole role = UserRole.CUSTOMER;

    @CreationTimestamp
    LocalDate createdDate;
}
