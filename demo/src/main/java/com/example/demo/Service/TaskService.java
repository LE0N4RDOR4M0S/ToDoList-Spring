package com.example.demo.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.Domain.Category;
import com.example.demo.Domain.Priority;
import com.example.demo.Domain.Task;
import com.example.demo.Repository.TaskRepository;

@Service
public class TaskService {

    @Autowired
    TaskRepository taskRepository;

    /**
     * Função para retornar uma tarefa pelo Id
     * @param id
     * @return Tarefa com id informado
     */
    public Task getTask(Long id){
        return taskRepository.findById(id).get();
    }

    /**
     * Função para retornar as task de cada usuário
     * @param id Id do Usuário
     * @return Lista de tarefas do usuário
     */
    public List<Task> usersTask(Long id){
        return taskRepository.findByIdUser(id);
    }

    /**
     * Função para criar uma nova tarefa
     *
     * @param task Nova tarefa
     */
    @Transactional
    public Task createTask(Long id,Task task) {
        task.setIdUser(id);
        return taskRepository.save(task);
    }

    /**
     * Função para deletar uma tarefa existente
     *
     * @param taskId Id da tarefa que deve ser deletada
     */
    @Transactional
    public void deleteTask(Long taskId) {
        taskRepository.deleteById(taskId);
    }

    /**
     * Função para atualizar uma tarefa
     *
     * @param taskId O Id da tarefa que deve ser atualizada
     *
     * @param updatedTask Nova tarefa
     */
    @Transactional
    public void updateTask(Long taskId, Task updatedTask) {
        Optional<Task> existingTaskOptional = taskRepository.findById(taskId);

        if (existingTaskOptional.isPresent()) {
            Task existingTask = existingTaskOptional.get();

            existingTask.setTitle(updatedTask.getTitle());
            existingTask.setDescription(updatedTask.getDescription());
            existingTask.setPriority(updatedTask.getPriority());
            existingTask.setFinalDate(updatedTask.getFinalDate());
            existingTask.setStatus(updatedTask.isStatus());

            taskRepository.save(existingTask);
        } else {
            throw new RuntimeException("Tarefa não encontrada com o ID: " + taskId);
        }
    }

        /**
     * Função para retornar uma lista de tarefas filtradas pelo usuário e categoria
     *
     * @param userId   Id do usuário pelo qual as tarefas devem ser filtradas
     * @param category Categoria que as tarefas devem ser filtradas
     * @return Lista de tarefas do usuário filtradas por categoria
     */
    @Transactional
    public List<Task> getTasksByUserAndCategory(Long userId, Category category) {
        return taskRepository.findByIdUserAndCategory(userId, category);
    }

    /**
     * Função para retornar uma lista de tarefas filtradas pelo usuário e status
     *
     * @param userId ID do usuário pelo qual as tarefas devem ser filtradas
     * @param status Status pelo qual as tarefas devem ser filtradas
     * @return Lista de tarefas do usuário e filtradas por status
     */
    @Transactional
    public List<Task> getTasksByUserAndStatus(Long userId, boolean status) {
        return taskRepository.findByIdUserAndStatus(userId, status);
    }

    /**
     * Função para retornar uma lista de tarefas filtradas pelo usuário e prioridade
     *
     * @param userId   ID do usuário pelo qual as tarefas devem ser filtradas
     * @param priority Prioridade pela qual as tarefas devem ser filtradas
     * @return Lista de tarefas do usuário filtradas por prioridade
     */
    @Transactional
    public List<Task> getTasksByUserAndPriority(Long userId, Priority priority) {
        return taskRepository.findByIdUserAndPriority(userId, priority);
    }

}
