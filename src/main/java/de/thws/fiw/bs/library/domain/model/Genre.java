package de.thws.fiw.bs.library.domain.model;

import java.util.HashSet;
import java.util.Set;

public class Genre {
    private Long id; // Die ID wird von der Datenbank generiert
    private String genrename;
    private String beschreibung;
    private Set<Book> books = new HashSet<>();;

    public Genre() {
    }

    public Genre(String genrename, String beschreibung) {
        this.genrename = genrename;
        this.beschreibung = beschreibung;
    }

    // Getter und Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGenrename() {
        return genrename;
    }

    public void setGenrename(String genrename) {
        this.genrename = genrename;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }
}
