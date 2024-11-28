package de.thws.fiw.bs.library.domain.ports;

import java.util.List;

import de.thws.fiw.bs.library.domain.model.Book;

public interface BookRepository {
    Book save(Book book); // Ein Buch speichern

    void delete(Long id); // Ein Buch löschen

    Book findById(Long id); // Buch nach ID finden

    List<Book> findAll(); // Alle Bücher abrufen

    List<Book> findByGenre(String genre); // Bücher nach Genre filtern

    List<Book> findByAuthorName(String authorName); // Bücher nach Autor filtern
}
