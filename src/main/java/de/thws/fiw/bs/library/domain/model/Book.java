package de.thws.fiw.bs.library.domain.model;

import java.util.Set;

public class Book {
    private Long id;
    private String title;
    private String isbn;
    private Set<Genre> genres;
    private Set<Author> authors;
    private boolean isAvailable;

    public Book() {
    }

    public Book(String title, String isbn, Set<Genre> genres, Set<Author> authors, boolean isAvailable) {
        this.title = title;
        this.isbn = isbn;
        this.genres = genres;
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

    public Set<Genre> getGenres() {
        return genres;
    }

    public void setGenres(Set<Genre> genres) {
        this.genres = genres;
    }

    public Set<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}
