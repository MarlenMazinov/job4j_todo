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
import java.time.ZoneId;
import java.util.List;


@Controller
@ThreadSafe
@AllArgsConstructor
public class TaskController {

    private final TaskService service;

    @GetMapping("/index")
    public String index(Model model) {
        model.addAttribute("tasks", service.findAllOrderByCreated());
        return "index";
    }

    @GetMapping("/executedTasks")
    public String executedTasks(Model model) {
        model.addAttribute("exTasks", service.findTasksByDone(true));
        return "listOfExecutedTasks";
    }

    @GetMapping("/unexecutedTasks")
    public String unexecutedTasks(Model model) {
        model.addAttribute("unexTasks", service.findTasksByDone(false));
        return "listOfUnexecutedTasks";
    }

    @GetMapping("/formAddTask")
    public String addTask(Model model) {
        model.addAttribute("task", new Task());
        model.addAttribute("allCategoriesList", service.findAllCategories());
        model.addAttribute("allPrioritiesList", service.findAllPriorities());
        return "addTask";
    }

    @GetMapping("/taskDescription/{taskId}")
    public String taskDescr(Model model, @PathVariable("taskId") int id) {
        if (service.findById(id).isPresent()) {
            model.addAttribute("task", service.findById(id).get());
        }
        return "taskDescription";
    }

    @GetMapping("/editTaskForm/{taskId}")
    public String editTask(Model model, @PathVariable("taskId") int id) {
        Task task = new Task();
        if (service.findById(id).isPresent()) {
            task = service.findById(id).get();
        }
        model.addAttribute("task", task);
        model.addAttribute("allCategoriesList", service.findAllCategories());
        model.addAttribute("allPrioritiesList", service.findAllPriorities());
        return "updateTaskForm";
    }

    @PostMapping("/createTask")
    public String createTask(@RequestParam(value = "category.id") List<Integer> findCategories,
                             @RequestParam(value = "priority.id") Integer priorityId,
                             @ModelAttribute Task task, HttpSession session) {
        task.setUser((User) session.getAttribute("user"));
        service.create(task, findCategories, priorityId);
        service.updateTaskPriority("urgently");
        return "redirect:/index";
    }

    @PostMapping("/editTaskCondition/{taskId}")
    public String editCondition(Model model, @PathVariable("taskId") int id,
                                HttpSession session) {
        service.updateTaskState(id);
        return taskDescr(model, id);
    }

    @PostMapping("/updateTask")
    public String updateTask(Model model,
                             @RequestParam(value = "category.id") List<Integer> findCategories,
                             @RequestParam(value = "priority.id") Integer priorityId,
                             @ModelAttribute Task task, HttpSession session) {
        task.setUser(service.findById(task.getId()).get().getUser());
        service.update(task, findCategories, priorityId);
        return taskDescr(model, task.getId());
    }

    @PostMapping("/deleteTask/{taskId}")
    public String deleteTask(@PathVariable("taskId") int id) {
        service.delete(id);
        return "redirect:/index";
    }
}
