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
    private ArrayList<User> users;
    private static User actualUser;
    private static List<Issue> issueList = new ArrayList<>();

    // Constructor
    public AuthService() {
        users = new ArrayList<>();
        users.add(new User("admin", "admin", "admin"));
        users.add(new User("usuario1", "usuario1", "cliente"));
        users.add(new User("usuario2", "usuario2", "cliente"));
        users.add(new User("usuario3", "usuario3", "cliente"));

        Issue firstIssue = new Issue("Problema con iniciar sesion", "usuario2", "Cuando pongo mi usuario y contraseña, se cierra la aplicacion.", "open", LocalDate.of(2024, 9, 29));
        firstIssue.addComment(new Comment("usuario1", "Me pasa lo mismo."));
        firstIssue.addComment(new Comment("admin", "Echaré un vistazo a ver."));

        issueList.add(firstIssue);
        issueList.add(new Issue("No puedo registrarme", "usuario3", "Cuando pongo mi usuario y contraseña, me dice datos erróneos.", "open", LocalDate.of(2024, 9, 28)));
        issueList.add(new Issue("Error cuando intento crear un nuevo issue", "usuario3", "Me aparece un error cuando intento crear un nuevo issue y no se porqué", "open", LocalDate.of(2024, 9, 30)));
    }

    public boolean register(User user) {
        for (User existingUser : users) {
            if (existingUser.getUsername().equals(user.getUsername())) {
                return false;
            }
        }
        user.setRole("cliente");
        users.add(user);
        return true;
    }

    public boolean login(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                actualUser = user;
                return true;
            }
        }
        return false;
    }

    public static User getActualUser() {
        return actualUser;
    }

    public void logout() {
        actualUser = null;
    }

    public static List<Issue> getIssueList() {
        return issueList;
    }

    public void addIssue(Issue issue) {
        issueList.add(issue);
    }

    public Issue getIssueById(int id) {
        for (Issue issue : issueList) {
            if (issue.getId() == id) {
                return issue;
            }
        }
        return null;
    }


    public void closeIssue(int id) {
        for (Issue issue : issueList) {
            if (issue.getId() == id && issue.getStatus().equals("open")) {
                issue.setStatus("closed");
                break;
            }
        }
    }


    public void reopenIssue(int id) {
        for (Issue issue : issueList) {
            if (issue.getId() == id && issue.getStatus().equals("closed")) {
                issue.setStatus("open");
                break;
            }
        }
    }
}
