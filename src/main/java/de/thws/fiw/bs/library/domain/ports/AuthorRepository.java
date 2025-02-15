package de.thws.fiw.bs.library.domain.ports;

import de.thws.fiw.bs.library.domain.model.Author;

import java.util.List;

public interface AuthorRepository {
    Author save(Author author); 

    void update(Author author);

    void delete(Long id); 

    Author findById(Long id); 

    List<Author> findAll(); 

    List<Author> getAuthorsByName(String name);
}
