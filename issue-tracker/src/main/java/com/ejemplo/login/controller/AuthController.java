package com.ejemplo.login.controller;
import com.ejemplo.login.model.Issue;
import com.ejemplo.login.model.User;
import com.ejemplo.login.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.ejemplo.login.model.Comment; // Asegúrate de importar la clase Comment
import java.time.LocalDate;
import java.util.List;

@Controller
public class AuthController {

    @Autowired
    private AuthService authService;

    @RequestMapping("/")
    public String redirectToRegister() {
        return "redirect:/register";  // Redirige a la página de registro
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
            User actualUser = AuthService.getActualUser(); // Acceder al usuario logueado
            model.addAttribute("message", "Bienvenido " + actualUser.getUsername());
            return "redirect:/issues"; // Puedes redirigir a una página home o similar
        } else {
            model.addAttribute("message", "Credenciales incorrectas");
            return "login";
        }
    }

    @GetMapping("/issues")
    public String issueList(Model model) {
        // Obtener la lista de issues desde el servicio y pasarla al modelo
        List<Issue> issues = AuthService.getIssueList();
        model.addAttribute("issues", issues);
        return "issueList"; // Nombre de la plantilla Thymeleaf
    }

    // Mostrar formulario para crear un nuevo issue
    @GetMapping("/issues/new")
    public String newIssueForm() {
        return "newIssue"; // Nombre de la plantilla Thymeleaf
    }

    // Procesar la creación de un nuevo issue
    @PostMapping("/issues")
    public String addIssue(@RequestParam String title, @RequestParam String description, Model model) {
        // Obtener el usuario actualmente logueado
        User actualUser = AuthService.getActualUser();

        if (actualUser != null) {
            // Crear un nuevo issue
            Issue newIssue = new Issue(title, actualUser.getUsername(), description, "open", LocalDate.now());
            authService.addIssue(newIssue); // Añadir el nuevo issue
            return "redirect:/issues"; // Redirigir a la lista de issues
        } else {
            model.addAttribute("message", "No hay usuario logueado");
            return "login"; // En caso de que no haya usuario logueado, redirige al login
        }
    }

    @GetMapping("/issues/{id}")
    public String viewIssueDetails(@PathVariable("id") int id, Model model) {
        Issue issue = authService.getIssueById(id);
        User currentUser = authService.getActualUser();

        // Agrega esto para depurar
        System.out.println("Usuario actual: " + (currentUser != null ? currentUser.getUsername() : "No hay usuario logueado"));
        System.out.println("Rol del usuario actual: " + (currentUser != null ? currentUser.getRole() : "No hay rol"));

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
        authService.closeIssue(id); // Lógica para cerrar el issue
        return "redirect:/issues"; // Redirige a la lista de issues
    }

    // Método para reabrir un issue
    @PostMapping("/issues/{id}/reopen")
    public String reopenIssue(@PathVariable("id") int id) {
        authService.reopenIssue(id); // Lógica para reabrir el issue
        return "redirect:/issues"; // Redirige a la lista de issues
    }

    @RequestMapping("/logout")
    public String logout() {
        authService.logout(); // Limpia el usuario activo
        return "redirect:/login"; // Redirige a la página de login
    }

    @PostMapping("/issues/{id}/comment")
    public String addComment(@PathVariable("id") int id, @RequestParam String username, @RequestParam String text, Model model) {
        System.out.println("Intentando agregar comentario para el Issue ID: " + id);
        System.out.println("Usuario: " + username);
        System.out.println("Texto del Comentario: " + text);

        Issue issue = authService.getIssueById(id); // Obtener el issue por ID
        if (issue != null) {
            Comment newComment = new Comment(username, text); // Crear una nueva instancia de Comment
            issue.addComment(newComment); // Añadir el comentario al issue
            System.out.println("Comentario añadido: " + newComment.getText());
            model.addAttribute("issue", issue); // Pasar el issue al modelo
            return "issueDetails"; // Redirigir a la vista de detalles del issue
        } else {
            System.out.println("Error: Issue no encontrado.");
            model.addAttribute("error", "Issue no encontrado"); // Manejo de error
            return "error"; // Redirigir a una página de error
        }
    }

    @RequestMapping("/issues/{id}/comment")
    public String commentForm(@PathVariable("id") int id, Model model) {
        Issue issue = authService.getIssueById(id); // Obtener el issue por ID
        if (issue != null) {
            model.addAttribute("issue", issue); // Pasar el issue al modelo
            model.addAttribute("username", authService.getActualUser().getUsername()); // Obtener el nombre del usuario actual
            System.out.println("Cargando formulario de comentario para el Issue ID: " + id);
        } else {
            System.out.println("Error: Issue no encontrado.");
        }
        return "addComment"; // Redirigir a la vista de añadir comentario
    }

}

