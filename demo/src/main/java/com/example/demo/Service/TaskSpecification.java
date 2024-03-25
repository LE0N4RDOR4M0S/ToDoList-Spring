package com.example.demo.Service;

import org.apache.tomcat.util.http.parser.Priority;
import org.springframework.data.jpa.domain.Specification;

import com.example.demo.Domain.Category;
import com.example.demo.Domain.Task;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

public class TaskSpecification {

    public static Specification<Task> byUserId(Long idUser) {
        return (root, query, criteriaBuilder) -> {
            if (idUser == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("idUser"), idUser);
        };
    }

    public static Specification<Task> filterByStatus(Boolean status) {
        return (Root<Task> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            if (status == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("status"), status);
        };
    }

    public static Specification<Task> filterByPriority(com.example.demo.Domain.Priority priority) {
        return (Root<Task> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            if (priority == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("priority"), priority);
        };
    }

    public static Specification<Task> filterByCategory(Category category) {
        return (Root<Task> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            if (category == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("category"), category);
        };
    }
}

