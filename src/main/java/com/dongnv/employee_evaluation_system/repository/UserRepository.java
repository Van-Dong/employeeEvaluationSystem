package com.dongnv.employee_evaluation_system.repository;

import com.dongnv.employee_evaluation_system.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
