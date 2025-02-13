package de.thws.fiw.bs.library.application.graphql;

import de.thws.fiw.bs.library.domain.model.*;
import de.thws.fiw.bs.library.domain.services.*;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import java.util.List;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class GraphQLResolvers implements GraphQLQueryResolver, GraphQLMutationResolver {

    private final BookService bookService;
    private final BorrowService borrowService;
    private final ReservationService reservationService;
    private final ReturnService returnService;
    private final UserService userService;

    public GraphQLResolvers(BookService bookService, BorrowService borrowService, ReservationService reservationService, ReturnService returnService, UserService userService) {
        this.bookService = bookService;
        this.borrowService = borrowService;
        this.reservationService = reservationService;
        this.returnService = returnService;
        this.userService = userService;
    }

    // Queries
    public Book getBookById(Long id) {
        return bookService.findBookById(id);
    }

    public List<Book> getBooks() {
        return bookService.getAllBooks();
    }

    public List<Book> getBooksByGenre(String genre) {
        return bookService.findBooksByGenre(genre);
    }

    public List<Book> getBooksByAuthor(String author) {
        return bookService.findBooksByAuthor(author);
    }

    public List<User> getUsers() {
        return userService.getAllUsers();
    }

    public Optional<User> getUserById(Long id) {
        return userService.findUserById(id);
    }

    // Mutations
    public Book addBook(Long id, String title, String isbn, String genre, List<String> authors, boolean isAvailable) {
        Book book = new Book(id, title, isbn, genre, new HashSet<>(authors), isAvailable);
        return bookService.addBook(book);
    }

    public void deleteBook(Long id) {
        bookService.deleteBook(id);
    }

    public User addUser(Long id, String name, String email, Set<Book> borrowedBooks) {
        User user = new User(id, name, email, borrowedBooks);
        return userService.addUser(user);
    }

    public void deleteUser(Long id) {
        userService.deleteUser(id);
    }

    public void borrowBook(Long bookId, Long userId) {
        borrowService.borrowBook(bookId, userId);
    }

    public void returnBook(Long bookId, Long userId) {
        returnService.returnBook(bookId, userId);
    }

    public void reserveBook(Long bookId, Long userId) {
        reservationService.reserveBook(bookId, userId);
    }
}
