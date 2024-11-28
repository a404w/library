package de.thws.fiw.bs.library.domain.model;

import java.util.Objects;
import java.util.Set;

public class Book {
    private Long id;
    private String title;
    private String isbn;
    private boolean isAvailable;
    private String genre;
    private Set<Author> authors;

    public Book(Long id, String title, String isbn, boolean isAvailable, String genre, Set<Author> authors) {
        this.id = id;
        this.title = title;
        this.isbn = isbn;
        this.isAvailable = isAvailable;
        this.genre = genre;
        this.authors = authors;
    }

    // Getter und Setter
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

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Set<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Book book = (Book) o;
        return Objects.equals(id, book.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
