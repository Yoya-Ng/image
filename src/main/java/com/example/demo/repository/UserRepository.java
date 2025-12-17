package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // Spring Data JPA 會自動實作這些方法
    User findByName(String name);
    
    List<User> findByAgeGreaterThan(Integer age);

    Optional<User> findByUsername(String username);
    
    boolean existsByUsername(String username);
}
