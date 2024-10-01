package com.ejemplo.login.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Issue {
    private static int idCounter = 0; // Para generar ID únicos

    private int id;
    private String title;
    private String user;
    private String description;
    private List<Comment> comments;
    private String status;
    private LocalDate date;

    public Issue(String title, String user, String description, String status, LocalDate date) {
        this.id = ++idCounter;
        this.title = title;
        this.user = user;
        this.description = description;
        this.comments = new ArrayList<>(); // Inicializar la lista de comentarios
        this.status = status;
        this.date = date;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    // Método para añadir un comentario
    public void addComment(Comment comment) {
        comments.add(comment);
    }
}
