package com.example.demo.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.example.demo.Domain.Category;
import com.example.demo.Domain.Priority;
import com.example.demo.Domain.Task;

public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {
    Optional<Task> findById(Long id);

    Optional<Task> findByTitle(String title);

    List<Task> findByIdUserAndCategory(Long idUser, Category category);

    List<Task> findByIdUserAndStatus(Long idUser, boolean status);

    List<Task> findByIdUserAndPriority(Long idUser, Priority priority);

    Page<Task> findByIdUser(Long idUser, Pageable pageable);
}
