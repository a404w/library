package de.thws.fiw.bs.library.application.graphql;
import de.thws.fiw.bs.library.domain.model.Book;
import de.thws.fiw.bs.library.domain.services.BookService;
import graphql.kickstart.tools.GraphQLMutationResolver;

import java.util.List;

public class BookMutationResolver implements GraphQLMutationResolver {

    private final BookService bookService;

    public BookMutationResolver(BookService bookService) {
        this.bookService = bookService;
    }

    /**
     * Entspricht 'addBook(...)' im Schema
     */
    public Book addBook(Long id,
                        String title,
                        String isbn,
                        String genre,
                        List<String> authors,
                        boolean isAvailable) {

        // Neues Book-Objekt erzeugen
        Book book = new Book(
                id, 
                title, 
                isbn, 
                genre, 
                authors, 
                isAvailable
        );

        // Über BookService speichern
        return bookService.addBook(book);
    }

    /**
     * Entspricht 'deleteBook(id: ID!)'
     */
    public Boolean deleteBook(Long id) {
        bookService.deleteBook(id);
        return true;
    }
}


