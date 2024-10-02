package com.ejemplo.login.controller;
import com.ejemplo.login.model.Issue;
import com.ejemplo.login.model.User;
import com.ejemplo.login.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.ejemplo.login.model.Comment;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class AuthController {

    @Autowired
    private AuthService authService;

    @RequestMapping("/")
    public String redirectToRegister() {
        return "redirect:/register";
    }

    @RequestMapping("/register")
    public String registerForm() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username, @RequestParam String password, Model model) {
        User user = new User(username, password, null);
        if (authService.register(user)) {
            model.addAttribute("message", "Registro exitoso");
        } else {
            model.addAttribute("message", "El usuario ya existe");
        }
        return "register";
    }

    @RequestMapping("/login")
    public String loginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, Model model) {
        if (authService.login(username, password)) {
            User actualUser = AuthService.getActualUser();
            model.addAttribute("message", "Bienvenido " + actualUser.getUsername());
            return "redirect:/issues";
        } else {
            model.addAttribute("message", "Credenciales incorrectas");
            return "login";
        }
    }

    @GetMapping("/issues")
    public String issueList(Model model) {
        List<Issue> issues = AuthService.getIssueList();
        model.addAttribute("issues", issues);
        return "issueList";
    }

    @GetMapping("/issues/new")
    public String newIssueForm() {
        return "newIssue";
    }

    @PostMapping("/issues")
    public String addIssue(@RequestParam String title,
                           @RequestParam String description,
                           @RequestParam(required = false) String labels,
                           Model model) {
        User actualUser = AuthService.getActualUser();

        if (actualUser != null) {
            List<String> labelList = new ArrayList<>();
            if (labels != null && !labels.isEmpty()) {
                labelList = Arrays.asList(labels.split("\\s*,\\s*"));
            }

            Issue newIssue = new Issue(title, actualUser.getUsername(), description, "open", LocalDate.now());
            newIssue.setLabels(labelList);
            authService.addIssue(newIssue);

            return "redirect:/issues";
        } else {
            model.addAttribute("message", "No hay usuario logueado");
            return "login";
        }
    }

    @GetMapping("/issues/{id}")
    public String viewIssueDetails(@PathVariable("id") int id, Model model) {
        Issue issue = authService.getIssueById(id);
        User currentUser = authService.getActualUser();

        if (issue != null) {
            model.addAttribute("issue", issue);
            model.addAttribute("currentUser", currentUser);
            return "issueDetails";
        } else {
            model.addAttribute("message", "El issue no existe.");
            return "redirect:/issues";
        }
    }


    @PostMapping("/issues/{id}/close")
    public String closeIssue(@PathVariable("id") int id) {
        authService.closeIssue(id);
        return "redirect:/issues";
    }

    // Método para reabrir un issue
    @PostMapping("/issues/{id}/reopen")
    public String reopenIssue(@PathVariable("id") int id) {
        authService.reopenIssue(id);
        return "redirect:/issues";
    }

    @RequestMapping("/logout")
    public String logout() {
        authService.logout();
        return "redirect:/login";
    }

    @PostMapping("/issues/{id}/comment")
    public String addComment(@PathVariable("id") int id, @RequestParam String text) {
        Issue issue = authService.getIssueById(id);
        User currentUser = authService.getActualUser(); // Obtiene el usuario actual

        if (issue != null && currentUser != null) {
            Comment newComment = new Comment(currentUser.getUsername(), text); // Usa el usuario actual
            issue.addComment(newComment);

            // Redirige a la vista de detalles del issue
            return "redirect:/issues/" + id; // Esto vuelve a cargar la página con el issue actualizado
        } else {
            return "error"; // Manejo de error si el issue no se encuentra
        }
    }


    @RequestMapping("/issues/{id}/comment")
    public String commentForm(@PathVariable("id") int id, Model model) {
        Issue issue = authService.getIssueById(id);
        if (issue != null) {
            model.addAttribute("issue", issue);
            model.addAttribute("username", authService.getActualUser().getUsername());
        }
        return "addComment";
    }

}

