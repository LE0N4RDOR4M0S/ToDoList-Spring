package com.example.demo.Domain;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskDetails {
    private String title;
    private String description;
    private LocalDate finalDate;
    private Priority priority;
}
