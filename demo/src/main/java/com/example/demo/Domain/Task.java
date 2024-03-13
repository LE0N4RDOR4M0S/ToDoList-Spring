package com.example.demo.Domain;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tarefa")
@EntityListeners(AuditingEntityListener.class)
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "usuario")
    private Long idUser;
    @Column(name = "titulo")
    private String title;
    @Column(name = "descricao")
    private String description;
    @Column(name = "data_criacao")
    private LocalDate creationDate;
    @Column(name = "data_fim")
    private LocalDate finalDate;
    @Column(name = "status")
    private boolean status;
    @Column(name = "prioridade")
    private Priority priority;
    @Column(name = "categoria")
    private Category category;

}
