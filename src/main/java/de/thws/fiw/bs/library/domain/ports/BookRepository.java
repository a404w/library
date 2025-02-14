package de.thws.fiw.bs.library.domain.ports;

import de.thws.fiw.bs.library.domain.model.Book;

import java.util.List;

public interface BookRepository {
    Book save(Book book);

    void update(Book book);

    void delete(Long id);

    Book findById(Long id);

    List<Book> findAll();

    List<Book> findByGenreId(Long id);

    List<Book> findByAuthorId(Long id);
}
