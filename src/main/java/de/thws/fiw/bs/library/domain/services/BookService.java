package de.thws.fiw.bs.library.domain.services;

import de.thws.fiw.bs.library.domain.model.Book;
import de.thws.fiw.bs.library.domain.ports.BookRepository;
import de.thws.fiw.bs.library.infrastructure.persistence.repository.BookRepositoryImpl;

import java.util.List;

public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }
    public BookService() {
        this.bookRepository = new BookRepositoryImpl();
    }

    public Book addBook(Book book) {
        return bookRepository.save(book); // Buch speichern
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id); // Finde Buch nach ID
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll(); // Alle Bücher abrufen
    }

    public List<Book> getBooksByGenreId(Long id) {
        return bookRepository.findByGenreId(id); // Bücher nach GenreId suchen
    }

    public List<Book> getBooksByAuthorId(Long id) {
        return bookRepository.findByAuthorId(id); // Bücher nach Autor suchen
    }

    public void deleteBook(Long id) {
        bookRepository.delete(id); // Buch löschen
    }

    public void updateBook(Book book) {
        bookRepository.update(book); // Buch aktualisieren
    }
}
