package ru.job4j.todo.controller;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.ZoneId;
import java.util.Optional;

@Controller
@ThreadSafe
@AllArgsConstructor
public class UserController {
    private final UserService service;

    @GetMapping("/loginOrRegistrationPage")
    public String loginOrRegistration() {
        return "loginOrRegistrationPage";
    }

    @GetMapping("/getRegistrationForm")
    public String addUser(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("timeZones", ZoneId.getAvailableZoneIds());
        return "registrationForm";
    }


    @PostMapping("/registration")
    public String registration(Model model, @ModelAttribute User user) {
        Optional<User> regUser = service.findByLoginAndPwd(user.getLogin(), user.getPassword());
        if (regUser.isPresent()) {
            model.addAttribute("message", "Такой пользователь в системе уже существует");
            return "fail";
        }
        service.create(user);
        return "login";
    }

    @GetMapping("/loginPage")
    public String loginPage(Model model,
                            @RequestParam(name = "fail", required = false) Boolean fail) {
        model.addAttribute("fail", fail != null);
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute User user, HttpServletRequest req) {
        Optional<User> userDb = service.findByLoginAndPwd(user.getLogin(), user.getPassword());
        if (userDb.isEmpty()) {
            return "redirect:/loginPage?fail=true";
        }
        HttpSession session = req.getSession();
        session.setAttribute("user", userDb.get());
        return "redirect:/index";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/loginOrRegistrationPage";
    }
}
