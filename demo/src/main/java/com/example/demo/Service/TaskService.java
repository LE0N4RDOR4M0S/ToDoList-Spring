package com.example.demo.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.Domain.Task;
import com.example.demo.Repository.TaskRepository;

public class TaskService {

    @Autowired
    TaskRepository taskRepository;

    /*Função para criar uma nova tarefa
     * @param task Nova tarefa
     */
    @Transactional
    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    /*Função para deletar uma tarefa existente
     * @param taskId Id da tarefa que deve ser deletada
    */
    @Transactional
    public void deleteTask(Long taskId) {
        taskRepository.deleteById(taskId);
    }

    /*Fução para atualizar uma tarefa
     * @param taskId O Id da tarefa que deve ser atualizada
     * @param updatedTask Nova tarefa
     */
    @Transactional
    public void updateTask(Long taskId,Task updatedTask) {
        Optional<Task> existingTaskOptional = taskRepository.findById(taskId);

        if (existingTaskOptional.isPresent()) {
            Task existingTask = existingTaskOptional.get();

            existingTask.setTitle(updatedTask.getTitle());
            existingTask.setDescription(updatedTask.getDescription());
            existingTask.setPriority(updatedTask.getPriority());
            existingTask.setFinalDate(updatedTask.getFinalDate());

            taskRepository.save(existingTask);
        } else {
            throw new RuntimeException("Tarefa não encontrada com o ID: " + taskId);
        }
    }

}
