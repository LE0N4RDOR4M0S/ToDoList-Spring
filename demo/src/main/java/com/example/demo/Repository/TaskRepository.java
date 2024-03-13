package com.example.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Domain.Category;
import com.example.demo.Domain.Priority;
import com.example.demo.Domain.Task;
import java.util.Optional;
import java.util.List;


public interface TaskRepository extends JpaRepository<Task, Long>{
    Optional<Task> findById(Long id);
    Optional<Task> findByTitle(String title);
    List<Task> findByCategory(Category category);
    List<Task> findByStatus(boolean status);
    List<Task> findByPriority(Priority priority);
    List<Task> findByIdUser(Long idUser);
}
