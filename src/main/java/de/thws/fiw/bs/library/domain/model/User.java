package de.thws.fiw.bs.library.domain.model;

import java.util.Set;

public class User {
    private Long id; // Die ID wird von der Datenbank generiert
    private String name;
    private String email;
    private Set<Book> borrowedBooks;

    public User() {
    }

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }
    

    public User(String name, String email, Set<Book> borrowedBooks) {
        this.name = name;
        this.email = email;
        this.borrowedBooks = borrowedBooks;
    }

    // Getter und Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Book> getBorrowedBooks() {
        return borrowedBooks;
    }

    public void setBorrowedBooks(Set<Book> borrowedBooks) {
        this.borrowedBooks = borrowedBooks;
    }
}
