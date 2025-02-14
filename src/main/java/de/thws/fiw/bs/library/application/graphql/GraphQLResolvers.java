package de.thws.fiw.bs.library.application.graphql;

import de.thws.fiw.bs.library.domain.model.*;
import de.thws.fiw.bs.library.domain.services.*;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

public class GraphQLResolvers implements GraphQLQueryResolver, GraphQLMutationResolver {

    private final BookService bookService;
    // private final BorrowService borrowService;
    private final LoanService loanService;
    private final ReservationService reservationService;
    // private final ReturnService returnService;
    private final UserService userService;
    private final AuthorService authorService; // 🔥 Fix: authorService hinzugefügt
    private final GenreService genreService;

    public GraphQLResolvers(
            BookService bookService,
            /* BorrowService borrowService, */
            LoanService loanService,
            ReservationService reservationService,
            GenreService genreService,
            /* ReturnService returnService, */
            UserService userService,
            AuthorService authorService) { // 🔥 Fix: Konstruktor angepasst
        this.bookService = bookService;
        // this.borrowService = borrowService;
        this.loanService = loanService;
        this.reservationService = reservationService;
        // this.returnService = returnService;
        this.userService = userService;
        this.authorService = authorService; // 🔥 Fix: Initialisierung hinzugefügt
        this.genreService = genreService;
    }

    // ===========================
    // 📚 BOOK QUERIES
    // ===========================

    public Book getBookById(Long id) {
        return bookService.getBookById(id); // Buch nach ID abrufen
    }

    public List<Book> getBooks() {
        return bookService.getAllBooks(); // Alle Bücher abrufen
    }

    public List<Book> getBooksByGenreId(Long id) {
        return bookService.getBooksByGenreId(id); // Bücher nach Genre abrufen
    }

    public List<Book> getBooksByAuthorId(Long id) {
        return bookService.getBooksByAuthorId(id); // Bücher nach Autor abrufen
    }

    // ===========================
    // 👥 USER QUERIES
    // ===========================
    public List<User> getUsers() {
        return userService.getAllUsers(); // Alle Nutzer abrufen
    }

    public User getUserById(Long id) {
        return userService.getUserById(id); // Nutzer nach ID abrufen
    }

    public User getUserByName(String name){
        return userService.getUserByName(name);
    }

    // ===========================
    // 📅 LOAN QUERIES
    // ===========================
    public List<Loan> getLoansByUser(Long userId) {
        return loanService.getLoansByUser(userId); // Ausleihen eines Nutzers abrufen
    }

    public List<Loan> getLoansByBook(Long bookId) {
        return loanService.getLoansByBook(bookId); // Ausleihen eines Buches abrufen
    }

    

    // ===========================
    // ✍️ AUTHOR QUERIES
    // ===========================

    public List<Author> getAllAuthors() {
        return authorService.getAllAuthors(); // Alle Autoren abrufen
    }

    public Author getAuthorById(Long id) {
        return authorService.getAuthorById(id); // Autor nach ID abrufen
    }

    public List<Author> getAuthorsByName(String name) {
        return authorService.getAuthorsByName(name); // Autoren nach Name suchen
    }

    // ===========================
    // 📚 BOOK MUTATIONS
    // ===========================

    public Book addBook(String title, String isbn, Set<Genre> genres, Set<Author> authors, boolean isAvailable) {
        return bookService.addBook(title, isbn, genres, authors, isAvailable); // Buch speichern
    }

    public boolean deleteBook(Long id) {
        bookService.deleteBook(id); // Buch löschen
        return true; // Rückgabe für GraphQL-Anfrage
    }

    public boolean updateBook(Long id, String title, String isbn, List<String> genres, List<Author> authors,
            boolean isAvailable) {
        Set<Genre> genreSet = new HashSet<>();
        for (String genreName : genres) {
            genreSet.add(new Genre(null, genreName, null)); // Genres hinzufügen
        }
        Book book = new Book(id, title, isbn, genreSet, new HashSet<>(authors), isAvailable);
        bookService.updateBook(book); // Buch aktualisieren
        return true; // Rückgabe für GraphQL
    }

    // public void borrowBook(Long bookId, Long userId) {
    // borrowService.borrowBook(bookId, userId); // Ausleihen
    // }

    // public void returnBook(Long bookId, Long userId) {
    // returnService.returnBook(bookId, userId); // Buch zurückgeben
    // }

    // ===========================
    // 📅 LOAN MUTATIONS
    // ===========================
    public Loan borrowBook(Long bookId, Long userId) {
        return loanService.borrowBook(bookId, userId); // Buch ausleihen
    }

    public String returnBook(Long loanId) {
        loanService.returnBook(loanId); // Buch zurückgeben
        return "Book returned successfully."; // Rückgabebestätigung
    }

    // ===========================
    // 📅 RESERVATION MUTATIONS
    // ===========================
    public void reserveBook(Long bookId, Long userId) {
        reservationService.reserveBook(bookId, userId); // Buch reservieren
    }

    // ===========================
    // 👥 USER MUTATIONS
    // ===========================
    public User addUser(Long id, String name, String email, Set<Book> borrowedBooks) {
        User user = new User(id, name, email, borrowedBooks); // Nutzer erstellen
        return userService.addUser(user); // Nutzer hinzufügen
    }

    public boolean deleteUser(Long id) {
        userService.deleteUser(id); // Nutzer löschen
        return true; // 🔥 Fix: Rückgabe für GraphQL-Anfrage
    }

    // ===========================
    // ✍️ AUTHOR MUTATIONS
    // ===========================

    public Author addAuthor(String name) {
        return authorService.addAuthor(name); // Autor hinzufügen
    }

    public boolean deleteAuthor(Long id) {
        authorService.deleteAuthor(id); // Autor löschen
        return true; // Rückgabe für GraphQL
    }

    public void updateAuthor(Author author){
        authorService.updateAuthor(author);
    }
}
