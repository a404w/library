package de.thws.fiw.bs.library.domain.model;

import java.util.Date;

public class Loan {
    private Book book;
    private User user;
    private Date from;
    private Date to;

    public Loan(Book book, User user, Date from, Date to) {
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

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
    }
}
