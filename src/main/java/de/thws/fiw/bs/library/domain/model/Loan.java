package de.thws.fiw.bs.library.domain.model;

import java.time.LocalDate;

public class Loan {
    private Long id; // Die ID wird von der Datenbank generiert
    private Book book;
    private User user;
    private LocalDate from;
    private LocalDate to;

    public Loan() {
    }

    public Loan(Book book, User user, LocalDate from, LocalDate to) {
        this.book = book;
        this.user = user;
        this.from = from;
        this.to = to;
    }

    // Getter und Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDate getFrom() {
        return from;
    }

    public void setFrom(LocalDate from) {
        this.from = from;
    }

    public LocalDate getTo() {
        return to;
    }

    public void setTo(LocalDate to) {
        this.to = to;
    }
}
