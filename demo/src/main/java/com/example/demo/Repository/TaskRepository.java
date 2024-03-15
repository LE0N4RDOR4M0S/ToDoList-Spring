package com.example.demo.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Domain.Category;
import com.example.demo.Domain.Priority;
import com.example.demo.Domain.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
    @SuppressWarnings("null")
    Optional<Task> findById(Long id);

    Optional<Task> findByTitle(String title);

    List<Task> findByIdUserAndCategory(Long userId, Category category);

    List<Task> findByIdUserAndStatus(Long userId, boolean status);

    List<Task> findByIdUserAndPriority(Long userId, Priority priority);

    List<Task> findByIdUser(Long idUser);
}
