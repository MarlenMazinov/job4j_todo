package ru.job4j.todo.filter;

import org.springframework.stereotype.Component;
import ru.job4j.todo.model.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@Component
public class AuthFilter implements Filter {
    private final Set<String> permittedUris = Set.of("login", "loginPage", "getRegistrationForm",
            "loginOrRegistrationPage", "registration", "registrationForm", "fail");

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String uri = req.getRequestURI();
        User user = (User) req.getSession().getAttribute("user");
        if (checkUri(uri) || (user != null)) {
            chain.doFilter(req, res);
            return;
        }
        res.sendRedirect(req.getContextPath() + "/loginOrRegistrationPage");
    }

    private boolean checkUri(String uri) {
        String[] arr = uri.split("/");
        return permittedUris.contains(arr[arr.length - 1]);
    }
}