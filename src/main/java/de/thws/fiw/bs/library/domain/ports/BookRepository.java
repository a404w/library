package de.thws.fiw.bs.library.domain.ports;

import java.util.List;

import de.thws.fiw.bs.library.domain.model.Book;

public interface BookRepository {
    List<Book> findAll();

    Book findById(Long id);

    List<Book> findByAuthorId(Long authorId);

    List<Book> findByGenre(String genre);

    void save(Book book);

    void delete(Long id);
}
