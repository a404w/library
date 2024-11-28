package de.thws.fiw.bs.library.domain.services;

import java.util.List;
import java.util.Optional;

import de.thws.fiw.bs.library.domain.model.Book;
import de.thws.fiw.bs.library.domain.ports.BookRepository;

public class BookService {
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    // Buch hinzufügen
    public Book addBook(Book book) {
        return bookRepository.save(book);
    }

    // Buch löschen
    public void deleteBook(Long bookId) {
        bookRepository.delete(bookId);
    }

    // Alle Bücher finden
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    // Buch nach ID finden
    public Optional<Book> findBookById(Long bookId) {
        return Optional.ofNullable(bookRepository.findById(bookId));
    }

    // Bücher nach Autor filtern
    public List<Book> findBooksByAuthor(String authorName) {
        return bookRepository.findByAuthorName(authorName);
    }

    // Bücher nach Genre filtern
    public List<Book> findBooksByGenre(String genre) {
        return bookRepository.findByGenre(genre);
    }
}
