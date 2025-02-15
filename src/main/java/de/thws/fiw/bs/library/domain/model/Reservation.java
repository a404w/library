package de.thws.fiw.bs.library.domain.model;

import java.time.LocalDateTime;

public class Reservation {
    private Long id; 
    private User user;
    private Book book;
    private LocalDateTime reservationDate;

    public Reservation() {
    }

    public Reservation(User user, Book book, LocalDateTime reservationDate) {
        this.user = user;
        this.book = book;
        this.reservationDate = reservationDate;
    }

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public LocalDateTime getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(LocalDateTime reservationDate) {
        this.reservationDate = reservationDate;
    }
}
