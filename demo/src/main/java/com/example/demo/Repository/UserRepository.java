package com.example.demo.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Domain.User;

public interface UserRepository extends JpaRepository<User, Long>{
    @SuppressWarnings("null")
    Optional<User> findById(Long id);
    Optional<User> findByName(String name);
}
