package com.example.demo.Controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.Domain.Category;
import com.example.demo.Domain.Priority;
import com.example.demo.Domain.Task;
import com.example.demo.Domain.TaskDetails;
import com.example.demo.Service.TaskService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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

    // Obter uma unica tarefa pelo seu id
    @ApiOperation(value = "Endpoint que retorna uma task pelo seu id, não é utilizado nos templates")
    @GetMapping("/{id}")
    public Task returnTask(@ApiParam(value = "Id da tarefa") @PathVariable Long id) {
        return taskService.getTask(id);
    }

    @ApiOperation(value = "Exibir formulário para criar uma nova tarefa")
    @GetMapping("/tasks/add/{id}")
    public ModelAndView showTaskForm(@ApiParam(value = "Id do usuário") @PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView("newTask.html");
        Task task = new Task();
        task.setIdUser(id);
        task.setStatus(false);
        task.setCreationDate(LocalDate.now());
        modelAndView.addObject("idUser", id);
        modelAndView.addObject("task", task);
        return modelAndView;
    }

    // Criar uma tarefa nova
    @ApiOperation(value = "Criar uma nova tarefa")
    @PostMapping("/task/add/{id}")
    public String addtaskUser(@ApiParam(value = "Nova Tarefa") @ModelAttribute Task task,
            @ApiParam(value = "Id do usuário") @PathVariable Long id) {
        try {
            taskService.createTask(id, task);
            return "redirect:/task/tasks/" + id;
        } catch (Exception e) {
            return "redirect:/error";
        }
    }

    // Retornar as tarefas de cada usuário
    @ApiOperation(value = "Retornar as tarefas do usuário")
    @GetMapping("/tasks/{id}")
    public ModelAndView userTasks(@ApiParam(value = "Id do usuário") @PathVariable Long id,
            @ApiParam(value = "Objeto para paginação das tarefas do usuário") Pageable pageable,
            @RequestParam(name = "status", required = false) Boolean status,
            @RequestParam(name = "priority", required = false) Priority priority,
            @RequestParam(name = "category", required = false) Category category) {
        ModelAndView modelAndView = new ModelAndView("tasks.html");
        Page<Task> tasksPage = taskService.usersTask(id, status, priority, category, PageRequest.of(pageable.getPageNumber(), 6));
        Long idUsuario = id;
        modelAndView.addObject("idUsuario", idUsuario);
        modelAndView.addObject("tasks", tasksPage);

        return modelAndView;
    }

    // Método auxiliar para obter o token JWT do cookie ou do cabeçalho de
    // autorização
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

    // Retornar as tarefas de cada usuário filtradas pelo status
    @ApiOperation(value = "Endpoint gerado para retornar tarefas do usuário filtradas pelo status pedido, não foi utilizado nos templates")
    @GetMapping("/status/{id}/{status}")
    public List<Task> statusTask(@ApiParam(value = "Id do usuário") @PathVariable Long id,
            @ApiParam(value = "status à ser filtrado") @PathVariable boolean status) {
        return taskService.getTasksByUserAndStatus(id, status);
    }

    // Retornar as tarefas de cada usuário filtradas pela categoria
    @ApiOperation(value = "Endpoint gerado para retornar tarefas do usuário filtradas pela categoria pedida, não foi utilizado nos templates")
    @GetMapping("/{id}/{categoria}")
    public List<Task> categoryTask(@ApiParam(value = "Id do usuário") @PathVariable Long id,
            @ApiParam(value = "Categoria das tarefas") @PathVariable Category category) {
        return taskService.getTasksByUserAndCategory(id, category);
    }

    // Retornar as tarefas de cada usuário filtradas pela prioridade
    @ApiOperation(value = "Endpoint gerado para retornar tarefas do usuário filtradas pela prioridade, não foi utilizado nos templates")
    @GetMapping("/{id}/{prioridade}")
    public List<Task> priorityTask(@ApiParam(value = "Id do usuário") @PathVariable Long id,
            @ApiParam(value = "Prioridade das tarefas") @PathVariable Priority priority) {
        return taskService.getTasksByUserAndPriority(id, priority);
    }

    // Alterar o status de uma tarefa
    @ApiOperation(value = "Endpoint que muda o valor do status da tarefa, entre true e false")
    @GetMapping("/status/{id}")
    public String changeStatus(@ApiParam(value = "Id da tarefa") @PathVariable Long id) {
        try {
            Task taskAlterada = taskService.getTask(id);
            taskAlterada.setStatus(!taskAlterada.isStatus());
            taskService.updateTask(id, taskAlterada);
            return "redirect:/task/tasks/" + taskAlterada.getIdUser();
        } catch (Exception e) {
            return "redirect:/error";
        }
    }

    @ApiOperation(value = "Formulário para alteração dos valores de uma tarefa")
    @GetMapping("/update/{id}")
    public ModelAndView updateTask(@PathVariable Long id) {
        Task taskAtual = taskService.getTask(id);
        ModelAndView modelAndView = new ModelAndView("updtask.html");
        modelAndView.addObject("task", taskAtual);
        return modelAndView;
    }

    @ApiOperation(value = "Alterar o titulo, descrição, data de conclusão e/ou a prioridade da tarefa")
    @PostMapping("/update/{id}")
    public String updateTaskDetails(@PathVariable Long id, @ModelAttribute TaskDetails taskDetails) {
        try {
            Task taskAlterada = taskService.getTask(id);
            taskAlterada.setTitle(taskDetails.getTitle());
            taskAlterada.setDescription(taskDetails.getDescription());
            taskAlterada.setFinalDate(taskDetails.getFinalDate());
            taskAlterada.setPriority(taskDetails.getPriority());
            taskService.updateTask(id, taskAlterada);
            return "redirect:/task/tasks/" + taskAlterada.getIdUser();
        } catch (Exception e) {
            return "redirect:/error";
        }
    }

    // Deletar uma tarefa
    @ApiOperation(value = "Deleta a tarefa pelo id")
    @GetMapping("/delete/{id}")
    public String deleteTask(@ApiParam(value = "Id da tarefa") @PathVariable Long id) {
        Long idUser = taskService.getTask(id).getIdUser();
        try {
            taskService.deleteTask(id);
            return "redirect:/task/tasks/" + idUser;
        } catch (Exception e) {
            return "redirect:/error";
        }
    }
}
