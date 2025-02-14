package de.thws.fiw.bs.library.domain.model;

import java.time.LocalDate;

public class Loan {
    private Book book;
    private User user;
    private LocalDate from;
    private LocalDate to;

    public Loan(Book book, User user, LocalDate from, LocalDate to) {
        this.book = book;
        this.user = user;
        this.from = from;
        this.to = to;
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
