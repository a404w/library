package de.thws.fiw.bs.library.application.graphql;

import de.thws.fiw.bs.library.domain.model.*;
import de.thws.fiw.bs.library.domain.services.*;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import java.util.List;

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
    // BOOK QUERIES
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
    // USER QUERIES
    // ===========================
    public List<User> getUsers() {
        return userService.getAllUsers(); // Alle Nutzer abrufen
    }

    public User getUserById(Long id) {
        return userService.getUserById(id); // Nutzer nach ID abrufen
    }

    public User getUserByName(String name) {
        return userService.getUserByName(name);
    }

    // ===========================
    // LOAN QUERIES
    // ===========================
    public List<Loan> getLoansByUser(Long userId) {
        return loanService.getLoansByUser(userId); // Ausleihen eines Nutzers abrufen
    }

    public List<Loan> getLoansByBook(Long bookId) {
        return loanService.getLoansByBook(bookId); // Ausleihen eines Buches abrufen
    }

    public List<Loan> getLoans() {
        return loanService.getAllLoans();
    }

    public Loan getLoanById(Long id) {
        return loanService.getLoanById(id);
    }

    // ===========================
    // AUTHOR QUERIES
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
    // GENRE QUERIES
    // ===========================

    public List<Genre> getAllGenres() {
        return genreService.getAllGenres();
    }

    public Genre getGenreById(Long id) {
        return genreService.getGenreById(id);
    }

    public Genre getGenresByName(String name) {
        return genreService.getGenresByName(name);
    }
    public Genre addGenre(String genrename, String beschreibung) {
        return genreService.addGenre(genrename, beschreibung);
    }
    public boolean deleteGenre(Long id) {
        genreService.deleteGenre(id);
        return true;
    }
    
    public boolean updateGenre(Genre genre) {
        genreService.updateGenre(genre);
        return true;
    }
    
    // ===========================
    // Reservation QUERIES
    // ===========================

    public List<Reservation> getAllReservations() {
        return reservationService.getAllReservations();
    }

    public Reservation getReservationById(Long id) {
        return reservationService.getReservationById(id);
    }

    public List<Reservation> getReservationsByUser(Long id) {
        return reservationService.getReservationsByUser(id);
    }

    public List<Reservation> getReservationByBook(Long id) {
        return reservationService.getReservationsByBook(id);
    }

    // ===========================
    // BOOK MUTATIONS
    // ===========================

    public Book addBook(Book book) {
        return bookService.addBook(book); // Buch speichern
    }

    public boolean deleteBook(Long id) {
        bookService.deleteBook(id); // Buch löschen
        return true; // Rückgabe für GraphQL-Anfrage
    }

    public boolean updateBook(Book book) {
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
    // LOAN MUTATIONS
    // ===========================
    public Loan addLoan(Book book, User user) {
        return loanService.addLoan(book, user); // Buch speichern
    }

    public boolean deleteLoan(Long id) {
        loanService.deleteLoan(id); // Buch löschen
        return true; // Rückgabe für GraphQL-Anfrage
    }

    public boolean updateLoan(Loan loan) {
        loanService.updateLoan(loan); // Buch aktualisieren
        return true; // Rückgabe für GraphQL
    }

    // ===========================
    // RESERVATION MUTATIONS
    // ===========================
    public Reservation addReservation(Book book, User user) {
        return reservationService.reserveBook(book, user);
    }

    public boolean deleteReservation(Long id) {
        reservationService.cancelReservation(id);
        return true;
    }

    public boolean updateReservation(Reservation reservation) {
        reservationService.updateReservation(reservation); // Buch aktualisieren
        return true; // Rückgabe für GraphQL
    }

    // ===========================
    // USER MUTATIONS
    // ===========================
    public User addUser(String name, String email) {
        return userService.addUser(name, email);
    }

    public boolean deleteUser(Long id) {
        userService.deleteUser(id); // Buch löschen
        return true; // Rückgabe für GraphQL-Anfrage
    }

    public boolean updateUser(User user) {
        userService.updateUser(user); // Buch aktualisieren
        return true; // Rückgabe für GraphQL
    }

    // ===========================
    // AUTHOR MUTATIONS
    // ===========================

    public Author addAuthor(String name) {
        return authorService.addAuthor(name); // Autor hinzufügen
    }

    public boolean deleteAuthor(Long id) {
        authorService.deleteAuthor(id); // Autor löschen
        return true; // Rückgabe für GraphQL
    }

    public boolean updateAuthor(Author author) {
        authorService.updateAuthor(author);
        return true; // Rückgabe für GraphQL
    }
}
