package com.example.demo.Controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Domain.Category;
import com.example.demo.Domain.Task;
import com.example.demo.Service.TaskService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import com.example.demo.Domain.Priority;


@RestController
@RequestMapping("/task")
@Api(value = "Gerenciamento de tarefas", description = "Endpoints para gerenciar a criação, a atualização e a filtração das tarefas em cada usuário")
public class TaskController {
    @Autowired
    TaskService taskService;

    //Obter uma unica tarefa pelo seu id
    @GetMapping("/{id}")
    public Task returnTask(@ApiParam(value = "Id da tarefa") @PathVariable Long id){
        return taskService.getTask(id);
    }


    //Criar uma tarefa nova
    @ApiOperation(value = "Criar uma nova tarefa")
    @PostMapping("/add/{id}")
    public String addUser ( @ApiParam(value = "Nova Tarefa") @ModelAttribute Task task,
                            @ApiParam(value = "Id do usuário") @PathVariable Long id){
        try{
            taskService.createTask(id,task);
            return "Tarefa Criada com sucesso!";
        }catch (Exception e){
            return "Erro na criação da tarefa";
        }
    }

    //Retornar as tarefas de cada usuário
    @ApiOperation(value = "Retornar as tarefas do usuário")
    @GetMapping("/user/{id}")
    public List<Task> userTasks(@ApiParam(value = "Id do usuário") @PathVariable Long id){
        return taskService.usersTask(id);
    }

    //Retornar as tarefas de cada usuário filtradas pelo status
    @GetMapping("/status/{id}/{status}")
    public List<Task> statusTask(@ApiParam(value = "Id do usuário") @PathVariable Long id,
                                @ApiParam(value = "status à ser filtrado") @PathVariable boolean status){
        return taskService.getTasksByUserAndStatus(id, status);
    }

    //Retornar as tarefas de cada usuário filtradas pela categoria
    @GetMapping("/{id}/{categoria}")
    public List<Task> categoryTask( @ApiParam(value = "Id do usuário") @PathVariable Long id,
                                    @ApiParam(value = "Categoria das tarefas") @PathVariable Category category){
        return taskService.getTasksByUserAndCategory(id, category);
    }

    //Retornar as tarefas de cada usuário filtradas pela prioridade
    @GetMapping("/{id}/{prioridade}")
    public List<Task> priorityTask( @ApiParam(value = "Id do usuário") @PathVariable Long id,
                                    @ApiParam(value = "Prioridade das tarefas") @PathVariable Priority priority){
        return taskService.getTasksByUserAndPriority(id, priority);
    }

    //Alterar o status de uma tarefa
    @GetMapping("status/{id}")
    public String changeStatus( @ApiParam(value = "Id da tarefa") @PathVariable Long id){
        try{
            Task taskAlterada = taskService.getTask(id);
            taskAlterada.setStatus(!taskAlterada.isStatus());
            taskService.updateTask(id, taskAlterada);
            return "Task "+id+" alterada com sucesso!";
        }catch (Exception e) {
            return "Houve um erro na alteração da task";
        }
    }

    //Alterar a prioridade de uma tarefa
    @GetMapping("/priority/{id}/{priority}")
    public String changePriority(@ApiParam(value = "Id da tarefa") @PathVariable Long id,
                                @ApiParam(value = "Nova prioridade da tarefa") @PathVariable Priority priority){
        try{
            Task taskAlterada = taskService.getTask(id);
            taskAlterada.setPriority(priority);
            taskService.updateTask(id, taskAlterada);
            return "Task "+id+" alterada com sucesso!";
        }catch (Exception e) {
            return "Houve um erro na alteração da tarefa";
        }
    }

    //Alterar o titulo/descrição/data final de uma tarefa
    @GetMapping("/name/{id}/{title}/{description}/{data}")
    public String updateTaskDetails(@ApiParam(value = "Id da tarefa") @PathVariable Long id,
                                    @ApiParam(value = "Novo titulo da tarefa") @PathVariable String title,
                                    @ApiParam(value = "Nova descrição da tarefa") @PathVariable String description,
                                    @ApiParam(value = "Nova data de conclusão da tarefa") @PathVariable LocalDate data){
        try{
            Task taskAlterada = taskService.getTask(id);
            taskAlterada.setTitle(title);
            taskAlterada.setDescription(description);
            taskAlterada.setFinalDate(data);
            taskService.updateTask(id, taskAlterada);
            return "Task "+id+" alterada com sucesso!";
        }catch (Exception e) {
            return "Houve um erro na alteração da task";
        }
    }

    //Deletar uma tarefa
    @GetMapping("/delete/{id}")
    public String deleteTask(@ApiParam(value = "Id da tarefa") @PathVariable Long id){
        try{
            taskService.deleteTask(id);
            return "Tarefa deletada com sucesso!";
        }catch (Exception e){
            return "Houve um erro para deletar a tarefa";
        }
    }
}
