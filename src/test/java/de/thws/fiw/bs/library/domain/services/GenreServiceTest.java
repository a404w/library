package de.thws.fiw.bs.library.domain.services;

import de.thws.fiw.bs.library.domain.model.Genre;
import de.thws.fiw.bs.library.domain.ports.GenreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GenreServiceTest {

    private GenreService genreService;
    private List<Genre> genres; // Direkte Speicherung ohne Repository-Klasse

    @BeforeEach
    void setUp() {
        genres = new ArrayList<>();
        genreService = new GenreService(new GenreRepository() {
            @Override
            public Genre save(Genre genre) {
                genre.setId((long) (genres.size() + 1)); // Simulierte Datenbank-ID
                genres.add(genre);
                return genre;
            }

            @Override
            public void update(Genre genre) {
                delete(genre.getId());
                genres.add(genre);
            }

            @Override
            public Genre findByName(String name) {
                return genres.stream()
                        .filter(g -> g.getGenrename().equalsIgnoreCase(name))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Genre not found"));
            }

            @Override
            public List<Genre> findAll() {
                return new ArrayList<>(genres);
            }

            @Override
            public Genre findById(Long id) {
                return genres.stream()
                        .filter(g -> g.getId().equals(id))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Genre not found"));
            }

            @Override
            public void delete(Long id) {
                genres.removeIf(g -> g.getId().equals(id));
            }
        });
    }

    @Test
    void addGenre_ShouldSaveGenre() {
        Genre genre = genreService.addGenre("Fantasy", "Fantasy-Bücher");
        assertNotNull(genre.getId()); // Die ID sollte gesetzt sein
        assertEquals("Fantasy", genre.getGenrename());
    }

    @Test
    void getGenreById_ShouldReturnCorrectGenre() {
        Genre savedGenre = genreService.addGenre("Science Fiction", "Zukunftsromane");
        Genre retrievedGenre = genreService.getGenreById(savedGenre.getId());

        assertEquals(savedGenre.getId(), retrievedGenre.getId());
        assertEquals("Science Fiction", retrievedGenre.getGenrename());
    }

    @Test
    void getAllGenres_ShouldReturnAllSavedGenres() {
        genreService.addGenre("Krimi", "Detektivgeschichten");
        genreService.addGenre("Horror", "Gruselgeschichten");

        List<Genre> genresList = genreService.getAllGenres();
        assertEquals(2, genresList.size());
    }

    @Test
    void deleteGenre_ShouldRemoveGenre() {
        Genre savedGenre = genreService.addGenre("Thriller", "Spannungsgeladene Geschichten");
        genreService.deleteGenre(savedGenre.getId());

        List<Genre> genresList = genreService.getAllGenres();
        assertEquals(0, genresList.size());
    }

    @Test
    void getGenresByName_ShouldReturnMatchingGenre() {
        genreService.addGenre("Roman", "Liebesgeschichten");
        genreService.addGenre("Drama", "Emotionale Erzählungen");

        Genre genre = genreService.getGenresByName("Roman");
        assertEquals("Roman", genre.getGenrename());
    }

    @Test
    void updateGenre_ShouldModifyExistingGenre() {
        Genre savedGenre = genreService.addGenre("Altes Genre", "Beschreibung");
        savedGenre.setGenrename("Neues Genre");
        genreService.updateGenre(savedGenre);

        Genre updatedGenre = genreService.getGenreById(savedGenre.getId());
        assertEquals("Neues Genre", updatedGenre.getGenrename());
    }
}
