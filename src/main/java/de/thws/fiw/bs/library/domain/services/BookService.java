package de.thws.fiw.bs.library.domain.services;

import de.thws.fiw.bs.library.domain.model.Author;
import de.thws.fiw.bs.library.domain.model.Book;
import de.thws.fiw.bs.library.domain.model.Genre;
import de.thws.fiw.bs.library.domain.ports.BookRepository;

import java.util.List;

public class BookService {
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Book addBook(Book book) {
        return bookRepository.save(book);
    }

    public void deleteBook(Long id) {
        bookRepository.delete(id);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book findBookById(Long id) {
        return bookRepository.findById(id);
    }

    public List<Book> findBooksByGenre(Genre genre) {
        return bookRepository.findByGenre(genre.getGenrename());
    }

    public List<Book> findBooksByAuthor(Author author) {
        return bookRepository.findByAuthor(author);
    }
}
