package de.thws.fiw.bs.library.application.graphql;
import de.thws.fiw.bs.library.domain.model.Book;
import de.thws.fiw.bs.library.domain.services.BookService;
import graphql.kickstart.tools.GraphQLQueryResolver;

import java.util.List;

public class BookQueryResolver implements GraphQLQueryResolver {

    private final BookService bookService;

    public BookQueryResolver(BookService bookService) {
        this.bookService = bookService;
    }

    // Entspricht 'books' im Schema
    public List<Book> books() {
        return bookService.getAllBooks();
    }

    // Entspricht 'bookById(id: ID!)' im Schema
    public Book bookById(Long id) {
        return bookService.findBookById(id);
    }

    // Entspricht 'booksByGenre(genre: String!)' im Schema
    public List<Book> booksByGenre(String genre) {
        return bookService.findBooksByGenre(genre);
    }

    // Falls du nach Autor(en) filtern willst, könntest du hier noch
    // public List<Book> booksByAuthor(String author) { ... }
    // hinzufügen und im Schema definieren
}

