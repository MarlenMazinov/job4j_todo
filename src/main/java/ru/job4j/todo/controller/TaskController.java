package ru.job4j.todo.controller;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.TaskService;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;


@Controller
@ThreadSafe
@AllArgsConstructor
public class TaskController {

    private final TaskService service;

    @GetMapping("/index")
    public String index(Model model, HttpSession session) {
        model.addAttribute("tasks", service.findAllOrderByCreated());
        return "index";
    }

    @GetMapping("/executedTasks")
    public String executedTasks(Model model, HttpSession session) {
        model.addAttribute("exTasks", service.findTasksByDone(true));
        return "listOfExecutedTasks";
    }

    @GetMapping("/unexecutedTasks")
    public String unexecutedTasks(Model model, HttpSession session) {
        model.addAttribute("unexTasks", service.findTasksByDone(false));
        return "listOfUnexecutedTasks";
    }

    @GetMapping("/formAddTask")
    public String addTask(Model model, HttpSession session) {
        model.addAttribute("newTask", new Task());
        return "addTask";
    }

    @GetMapping("/taskDescription/{taskId}")
    public String taskDescr(Model model, @PathVariable("taskId") int id,
                            HttpSession session) {
        if (service.findById(id).isPresent()) {
            model.addAttribute("task", service.findById(id).get());
        }
        return "taskDescription";
    }

    @GetMapping("/editTaskForm/{taskId}")
    public String editTask(Model model, @PathVariable("taskId") int id,
                           HttpSession session) {
        Task task = new Task();
        if (service.findById(id).isPresent()) {
            task = service.findById(id).get();
        }
        model.addAttribute("task", task);
        return "updateTaskForm";
    }

    @PostMapping("/createTask")
    public String createTask(Model model, @ModelAttribute Task task, HttpSession session) {

        task.setCreated(LocalDateTime.now());
        task.setUser((User) session.getAttribute("user"));
        service.create(task);
        service.updateTaskPriority("urgently");
        return "redirect:/index";
    }

    @PostMapping("/editTaskCondition/{taskId}")
    public String editCondition(Model model, @PathVariable("taskId") int id,
                                HttpSession session) {
        service.updateTaskState(id);
        return taskDescr(model, id, session);
    }

    @PostMapping("/updateTask")
    public String updateTask(Model model, @ModelAttribute Task task, HttpSession session) {
        service.update(task);
        return taskDescr(model, task.getId(), session);
    }

    @PostMapping("/deleteTask/{taskId}")
    public String deleteTask(Model model, @PathVariable("taskId") int id,
                             HttpSession session) {
        service.delete(id);
        return "redirect:/index";
    }
}
