package de.thws.fiw.bs.library.domain.ports;

import de.thws.fiw.bs.library.domain.model.Genre;
import java.util.List;

public interface GenreRepository {
    Genre save(Genre genre); // Speichert ein neues Genre

    void update(Genre genre);

    void delete(Long id); // Löscht ein Genre nach ID

    Genre findById(Long id);

    Genre findByName(String name); // Findet Genres nach Name

    List<Genre> findAll(); // Gibt alle Genres zurück
}
