package com.dongnv.employee_evaluation_system.model;

import java.time.LocalDate;

import jakarta.persistence.*;

import org.hibernate.annotations.CreationTimestamp;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Evaluation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    Boolean isCommended;
    String reason;

    @CreationTimestamp
    LocalDate evaluationDate;

    @ManyToOne
    Employee employee;
}
