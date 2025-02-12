package de.thws.fiw.bs.library.domain.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.thws.fiw.bs.library.domain.model.Book;
import de.thws.fiw.bs.library.domain.ports.BookRepository;

class MockBookRepository implements BookRepository {
    private final Map<Long, Book> books = new HashMap<>();
    private long currentId = 1;

    @Override
    public Book save(Book book) {
        if (book.getId() == null) {
            book.setId(currentId++); // Neue ID generieren, falls das Buch noch keine hat
        }
        books.put(book.getId(), book); // Buch in der Map speichern
        return book; // Gespeichertes Buch zurückgeben
    }

    @Override
    public void delete(Long id) {
        books.remove(id); // Buch aus der Map entfernen
    }

    @Override
    public Book findById(Long id) {
        return books.get(id); // Buch anhand der ID zurückgeben
    }

    @Override
    public List<Book> findAll() {
        return new ArrayList<>(books.values()); // Alle Bücher als Liste zurückgeben
    }

    @Override
    public List<Book> findByGenre(String genre) {
        return books.values().stream()
                .filter(book -> genre.equals(book.getGenre())) // Bücher nach Genre filtern
                .toList();
    }

    @Override
    public List<Book> findByAuthor(String author) {
        return books.values().stream()
                .filter(book -> book.getAuthors().contains(author)) // Bücher nach Autor filtern
                .toList();
    }
    @Override
    public void update(Book book) {
        if (books.containsKey(book.getId())) {
            books.put(book.getId(), book); // Buch mit neuen Werten ersetzen
        }
    }
}
