package com.dongnv.employee_evaluation_system.model;

import com.dongnv.employee_evaluation_system.constant.UserRole;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(unique = true, nullable = false)
    String username;
    String password;

    @Builder.Default
    Boolean isActive = false;

    @Enumerated(EnumType.STRING)  // Save name of enum in database. EnumType.ORDINAL will save index
    private UserRole role;

    @CreationTimestamp
    LocalDate createdDate;
}
