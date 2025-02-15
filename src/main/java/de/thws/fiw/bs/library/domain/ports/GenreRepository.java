package de.thws.fiw.bs.library.domain.ports;

import de.thws.fiw.bs.library.domain.model.Genre;
import java.util.List;

public interface GenreRepository {
    Genre save(Genre genre);

    void update(Genre genre);

    void delete(Long id); 

    Genre findById(Long id);

    Genre findByName(String name); 

    List<Genre> findAll(); 
}
