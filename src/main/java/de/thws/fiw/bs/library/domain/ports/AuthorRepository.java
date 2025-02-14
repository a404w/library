package de.thws.fiw.bs.library.domain.ports;

import de.thws.fiw.bs.library.domain.model.Author;

import java.util.List;

public interface AuthorRepository {
    Author save(Author author); // Speichert einen neuen Author

    void update(Author author);

    void delete(Long id); // Löscht einen Author nach ID

    Author findById(Long id); // Findet einen Author nach ID

    List<Author> findAll(); // Gibt alle Autoren zurück

    List<Author> getAuthorsByName(String name);
}
