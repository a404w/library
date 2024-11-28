package de.thws.fiw.bs.library.domain.ports;

import de.thws.fiw.bs.library.domain.model.Book;

import java.util.List;

public interface BookRepository {
    Book save(Book book); // Speichert ein Buch

    void delete(Long id); // Löscht ein Buch

    Book findById(Long id); // Sucht ein Buch anhand der ID

    List<Book> findAll(); // Gibt alle Bücher zurück

    List<Book> findByGenre(String genre); // Filtert Bücher nach Genre

    List<Book> findByAuthor(String author); // Filtert Bücher nach Autor
}
