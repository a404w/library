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

    public GraphQLResolvers(BookService bookService, BorrowService borrowService, ReservationService reservationService,
            ReturnService returnService, UserService userService) {
        this.bookService = bookService;
        this.borrowService = borrowService;
        this.reservationService = reservationService;
        this.returnService = returnService;
        this.userService = userService;
    }

    // Queries
    // Book
    public Book getBookById(Long id) {
        return bookService.findBookById(id);
    }

    public List<Book> getBooks() {
        return bookService.getAllBooks();
    }

    public List<Book> getBooksByGenre(String genreName) {
        return bookService.findBooksByGenre(new Genre(null, genreName, null));
    }

    public List<Book> getBooksByAuthor(Author author) {
        return bookService.findBooksByAuthor(author);
    }

    // User
    public List<User> getUsers() {
        return userService.getAllUsers();
    }

    public User getUserById(Long id) {
        return userService.findUserById(id);
    }

    // Mutations
    // Book
    public Book addBook(Long id, String title, String isbn, List<String> genres, List<Author> authors,
            boolean isAvailable) {
        Set<Genre> genreSet = new HashSet<>();
        for (String genreName : genres) {
            genreSet.add(new Genre(null, genreName, null));
        }

        Book book = new Book(id, title, isbn, genreSet, new HashSet<>(authors), isAvailable);
        return bookService.addBook(book);
    }

    public void deleteBook(Long id) {
        bookService.deleteBook(id);
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

    // User
    public User addUser(Long id, String name, String email, Set<Book> borrowedBooks) {
        User user = new User(id, name, email, borrowedBooks);
        return userService.addUser(user);
    }

    public void deleteUser(Long id) {
        userService.deleteUser(id);
    }
}
