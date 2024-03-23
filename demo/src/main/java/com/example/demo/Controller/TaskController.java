package com.example.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.Domain.Category;
import com.example.demo.Domain.Task;
import com.example.demo.Domain.TaskDetails;
import com.example.demo.Service.TaskService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import com.example.demo.Domain.Priority;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.time.LocalDate;

@Controller
@RequestMapping("/task")
@Api(value = "Gerenciamento de tarefas", description = "Endpoints para gerenciar a criação, a atualização e a filtração das tarefas em cada usuário")
public class TaskController {
    @Autowired
    TaskService taskService;

    @Autowired
    HttpServletRequest request;

    @Autowired
    HttpServletResponse response;

    @Autowired
    RestTemplate restTemplate;

    //Obter uma unica tarefa pelo seu id
    @GetMapping("/{id}")
    public Task returnTask(@ApiParam(value = "Id da tarefa") @PathVariable Long id){
        return taskService.getTask(id);
    }

    @ApiOperation(value = "Exibir formulário para criar uma nova tarefa")
    @GetMapping("/tasks/add/{id}")
    public String showTaskForm(@PathVariable Long id, Model model) {
        model.addAttribute("userId", id);
        Task task = new Task();
        task.setIdUser(id);
        task.setStatus(false);
        task.setCreationDate(LocalDate.now());
        model.addAttribute("idUser", id);
        model.addAttribute("task", task);
        return "newTask.html";
    }

    //Criar uma tarefa nova
    @ApiOperation(value = "Criar uma nova tarefa")
    @PostMapping("/task/add/{id}")
    public String addtaskUser ( @ApiParam(value = "Nova Tarefa") @ModelAttribute Task task,
                            @ApiParam(value = "Id do usuário") @PathVariable Long id){
        try{
            taskService.createTask(id,task);
            return "redirect:/task/tasks/"+id;
        }catch (Exception e){
            return "redirect:/error";
        }
    }

    // Retornar as tarefas de cada usuário
    @ApiOperation(value = "Retornar as tarefas do usuário")
    @GetMapping("/tasks/{id}")
    public ModelAndView userTasks(@ApiParam(value = "Id do usuário") @PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView("tasks.html");
        List<Task> tasks = taskService.usersTask(id);
        Long idUsuario = id;
        modelAndView.addObject("idUsuario", idUsuario);
        modelAndView.addObject("tasks", tasks);
        return modelAndView;
    }

    // Método auxiliar para obter o token JWT do cookie ou do cabeçalho de autorização
    private String getTokenFromRequest() {
        String token = null;
        // Primeiro, tenta obter o token do cookie
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("jwt-token")) {
                    token = cookie.getValue();
                    break;
                }
            }
        }
        // Se não encontrou o token no cookie, tenta obter do cabeçalho de autorização
        if (token == null) {
            token = request.getHeader("Authorization");
        }
        return token;
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
    @GetMapping("/status/{id}")
    public String changeStatus( @ApiParam(value = "Id da tarefa") @PathVariable Long id){
        try{
            Task taskAlterada = taskService.getTask(id);
            taskAlterada.setStatus(!taskAlterada.isStatus());
            taskService.updateTask(id, taskAlterada);
            return "redirect:/task/tasks/"+taskAlterada.getIdUser();
        }catch (Exception e) {
            return "redirect:/error";
        }
    }

    //Alterar a prioridade de uma tarefa
    @PostMapping("/priority/{id}/{priority}")
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
    @PostMapping("/name/{id}/{title}/{description}/{data}")
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

    @GetMapping("/update/{id}")
    public ModelAndView updateTask(@PathVariable Long id){
        Task taskAtual = taskService.getTask(id);
        ModelAndView modelAndView = new ModelAndView("updtask.html");
        modelAndView.addObject("task", taskAtual);
        return modelAndView;
    }

    @ApiOperation(value = "Alterar o titulo, descrição, data de conclusão ou a prioridade da tarefa")
    @PostMapping("/update/{id}")
    public String updateTaskDetails(@PathVariable Long id, @ModelAttribute TaskDetails taskDetails) {
        try {
            Task taskAlterada = taskService.getTask(id);
            taskAlterada.setTitle(taskDetails.getTitle());
            taskAlterada.setDescription(taskDetails.getDescription());
            taskAlterada.setFinalDate(taskDetails.getFinalDate());
            taskAlterada.setPriority(taskDetails.getPriority());
            taskService.updateTask(id, taskAlterada);
            return "redirect:/task/tasks/"+taskAlterada.getIdUser();
        } catch (Exception e) {
            return "redirect:/error";
        }
    }

    //Deletar uma tarefa
    @GetMapping("/delete/{id}")
    public String deleteTask(@ApiParam(value = "Id da tarefa") @PathVariable Long id){
        Long idUser = taskService.getTask(id).getIdUser();
        try{
            taskService.deleteTask(id);
            return "redirect:/task/tasks/"+idUser;
        }catch (Exception e){
            return "redirect:/error";
        }
    }
}
