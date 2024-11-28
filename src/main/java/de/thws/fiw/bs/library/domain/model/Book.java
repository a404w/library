package de.thws.fiw.bs.library.domain.model;

import java.util.Set;

public class Book {
    private Long id;
    private String title;
    private String isbn;
    private String genre;
    private Set<String> authors;
    private boolean isAvailable;

    public Book(Long id, String title, String isbn, String genre, Set<String> authors, boolean isAvailable) {
        this.id = id;
        this.title = title;
        this.isbn = isbn;
        this.genre = genre;
        this.authors = authors;
        this.isAvailable = isAvailable;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Set<String> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<String> authors) {
        this.authors = authors;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}
