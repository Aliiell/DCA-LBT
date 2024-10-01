package com.ejemplo.login.service;
import com.ejemplo.login.model.Issue;
import com.ejemplo.login.model.Comment;
import com.ejemplo.login.model.User;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class AuthService {
    private ArrayList<User> users; // Lista de usuarios
    private static User actualUser; // Usuario actualmente logueado (estático)
    private static List<Issue> issueList = new ArrayList<>(); // Lista de issues

    // Constructor
    public AuthService() {
        users = new ArrayList<>(); // Inicializa la lista
        users.add(new User("admin", "admin", "admin")); // Usuario admin
        users.add(new User("u1", "u1", "cliente"));
        users.add(new User("u2", "u2", "cliente"));
        users.add(new User("u3", "u3", "cliente"));

        // Inicializa algunas issues
        Issue firstIssue = new Issue("Problema para iniciar sesion", "u2", "Cuando pongo mi usuario y contraseña, se cierra la aplicacion.", "open", LocalDate.of(2022, 9, 30));
        firstIssue.addComment(new Comment("u1", "He tenido el mismo problema."));
        firstIssue.addComment(new Comment("admin", "Voy a revisarlo."));

        issueList.add(firstIssue);
        issueList.add(new Issue("No se puede registrar", "u3", "Cuando pongo mi usuario y contraseña, me devuelve a la página principal.", "open", LocalDate.of(2022, 9, 29)));
        issueList.add(new Issue("Error al crear nuevo issue", "u3", "Le doy a crear nuevo issue, y me aparece error, ¿qué hago?", "open", LocalDate.of(2022, 9, 30)));
    }

    public boolean register(User user) {
        // Verificar si el usuario ya existe
        for (User existingUser : users) {
            if (existingUser.getUsername().equals(user.getUsername())) {
                return false; // Usuario ya registrado
            }
        }
        user.setRole("cliente"); // Establecer el rol como cliente
        users.add(user); // Agregar el nuevo usuario
        return true;
    }

    public boolean login(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                actualUser = user; // Establece el usuario actual
                return true; // Credenciales correctas
            }
        }
        return false; // Credenciales incorrectas
    }

    public static User getActualUser() {
        return actualUser; // Devuelve el usuario actualmente logueado
    }

    public void logout() {
        actualUser = null; // Limpia el usuario activo al cerrar sesión
    }

    public static List<Issue> getIssueList() {
        return issueList; // Retorna la lista de issues
    }

    public void addIssue(Issue issue) {
        issueList.add(issue); // Añadir nuevo issue a la lista
    }

    // Método para obtener un issue por su ID
    public Issue getIssueById(int id) {
        for (Issue issue : issueList) {
            if (issue.getId() == id) {
                return issue; // Retorna el issue si coincide el ID
            }
        }
        return null; // Si no se encuentra, retorna null
    }

    // Método para cerrar un issue
    public void closeIssue(int id) {
        for (Issue issue : issueList) {
            if (issue.getId() == id && issue.getStatus().equals("open")) {
                issue.setStatus("closed"); // Cambiar el estado a cerrado
                break;
            }
        }
    }

    // Método para reabrir un issue
    public void reopenIssue(int id) {
        for (Issue issue : issueList) {
            if (issue.getId() == id && issue.getStatus().equals("closed")) {
                issue.setStatus("open"); // Cambiar el estado a abierto
                break;
            }
        }
    }
}
