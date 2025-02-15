package de.thws.fiw.bs.library.domain.services;

import de.thws.fiw.bs.library.domain.model.Genre;
import de.thws.fiw.bs.library.domain.ports.GenreRepository;
import java.util.List;

public class GenreService {

    private final GenreRepository genreRepository;

    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public Genre addGenre(String genrename, String beschreibung) {
        return genreRepository.save(new Genre(genrename, beschreibung)); 
    }

    public void updateGenre(Genre genre) {
        genreRepository.update(genre); 
    }

    public Genre getGenresByName(String name) {
        return genreRepository.findByName(name); 
    }

    public List<Genre> getAllGenres() {
        return genreRepository.findAll(); 
    }

    public Genre getGenreById(Long id) {
        return genreRepository.findById(id);
    }

    public void deleteGenre(Long id) {
        genreRepository.delete(id); 
    }
}
