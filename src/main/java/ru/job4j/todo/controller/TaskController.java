package ru.job4j.todo.controller;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.service.TaskService;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Calendar;


@Controller
@ThreadSafe
@AllArgsConstructor
public class TaskController {

    private final TaskService service;

    @GetMapping("/index")
    public String index(Model model, HttpSession session) {
        if (checkUser(model, session) != null) {
            return checkUser(model, session);
        }
        model.addAttribute("tasks", service.findAllOrderByCreated());
        return "index";
    }

    @GetMapping("/executedTasks")
    public String executedTasks(Model model, HttpSession session) {
        if (checkUser(model, session) != null) {
            return checkUser(model, session);
        }
        model.addAttribute("exTasks", service.findTasksByDone(true));
        return "listOfExecutedTasks";
    }

    @GetMapping("/unexecutedTasks")
    public String unexecutedTasks(Model model, HttpSession session) {
        if (checkUser(model, session) != null) {
            return checkUser(model, session);
        }
        model.addAttribute("unexTasks", service.findTasksByDone(false));
        return "listOfUnexecutedTasks";
    }

    @GetMapping("/formAddTask")
    public String addTask(Model model, HttpSession session) {
        if (checkUser(model, session) != null) {
            return checkUser(model, session);
        }
        model.addAttribute("newTask", new Task());
        return "addTask";
    }

    @GetMapping("/taskDescription/{taskName}")
    public String taskDescr(Model model, @PathVariable("taskName") String name,
                            HttpSession session) {
        if (checkUser(model, session) != null) {
            return checkUser(model, session);
        }
        if (service.findByName(name).isPresent()) {
            model.addAttribute("task", service.findByName(name).get());
        }
        return "taskDescription";
    }

    @GetMapping("/editTaskForm/{taskName}")
    public String editTask(Model model, @PathVariable("taskName") String name,
                           HttpSession session) {
        if (checkUser(model, session) != null) {
            return checkUser(model, session);
        }
        Task task = getTaskFrBd(name);
        model.addAttribute("task", task);
        return "updateTaskForm";
    }

    @PostMapping("/createTask")
    public String createTask(Model model, @ModelAttribute Task task, HttpSession session) {
        if (checkUser(model, session) != null) {
            return checkUser(model, session);
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        task.setCreated(dateFormat.format(Calendar.getInstance().getTime()));
        service.create(task);
        return "redirect:/index";
    }

    @PostMapping("/editTaskCondition/{taskName}")
    public String editCondition(Model model, @PathVariable("taskName") String name,
                                HttpSession session) {
        if (checkUser(model, session) != null) {
            return checkUser(model, session);
        }
        Task task = getTaskFrBd(name);
        task.setDone(true);
        service.update(task);
        return taskDescr(model, name, session);
    }

    @PostMapping("/updateTask")
    public String updateTask(Model model, @ModelAttribute Task task, HttpSession session) {
        if (checkUser(model, session) != null) {
            return checkUser(model, session);
        }
        service.update(task);
        return taskDescr(model, task.getName(), session);
    }

    @PostMapping("/deleteTask/{taskName}")
    public String deleteTask(Model model, @PathVariable("taskName") String name,
                             HttpSession session) {
        if (checkUser(model, session) != null) {
            return checkUser(model, session);
        }
        Task task = getTaskFrBd(name);
        service.delete(task);
        return "redirect:/index";
    }

    private <T> Task getTaskFrBd(T param) {
        Task result = new Task();
        if (param.getClass().equals(String.class)) {
            if (service.findByName((String) param).isPresent()) {
                result = service.findByName((String) param).get();
            }
        } else {
            if (service.findById((Integer) param).isPresent()) {
                result = service.findById((Integer) param).get();
            }
        }
        return result;
    }

    private String checkUser(Model model, HttpSession session) {
        String result = null;
        if (session.getAttribute("user") == null) {
            result = "/loginOrRegistrationPage";
        } else {
            model.addAttribute("user", session.getAttribute("user"));
        }
        return result;
    }
}
