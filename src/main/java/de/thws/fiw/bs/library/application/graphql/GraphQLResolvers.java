package de.thws.fiw.bs.library.application.graphql;

import de.thws.fiw.bs.library.domain.model.*;
import de.thws.fiw.bs.library.domain.services.*;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import java.util.List;

public class GraphQLResolvers implements GraphQLQueryResolver, GraphQLMutationResolver {

    private final BookService bookService;
    private final LoanService loanService;
    private final ReservationService reservationService;
    private final UserService userService;
    private final AuthorService authorService;
    private final GenreService genreService;

    // Im Konstruktor alle Services entgegennehmen
    public GraphQLResolvers(
            BookService bookService,
            LoanService loanService,
            ReservationService reservationService,
            GenreService genreService,
            UserService userService,
            AuthorService authorService) {
        this.bookService = bookService;
        this.loanService = loanService;
        this.reservationService = reservationService;
        this.genreService = genreService;
        this.userService = userService;
        this.authorService = authorService;
    }

    // ----------------------------------------------------------------------------
    // QUERIES
    // ----------------------------------------------------------------------------

    // -- Bücher
    public List<Book> getBooks() {
        return bookService.getAllBooks();
    }

    public Book getBookById(Long id) {
        return bookService.getBookById(id);
    }

    public List<Book> getBooksByGenreId(Long id) {
        return bookService.getBooksByGenreId(id);
    }

    public List<Book> getBooksByAuthorId(Long id) {
        return bookService.getBooksByAuthorId(id);
    }

    // -- Benutzer
    public List<User> getUsers() {
        return userService.getAllUsers();
    }

    public User getUserById(Long id) {
        System.out.println("🔎 GraphQL Resolver: getUserById aufgerufen mit ID: " + id);

        User user = userService.getUserById(id);

        if (user == null) {
            System.out.println("❌ GraphQL Resolver: Kein Benutzer mit ID " + id + " gefunden.");
            return null;
        } else {
            System.out.println("✅ GraphQL Resolver: Benutzer zurückgegeben: ID=" + user.getId() + ", Name="
                    + user.getName() + ", Email=" + user.getEmail());
        }

        // ❗ TEST: Setze borrowedBooks auf null, um Serialisierungsprobleme zu testen
        user.setBorrowedBooks(null);

        return user;
    }

    public User getUserByName(String name) {
        return userService.getUserByName(name);
    }

    // -- Ausleihen/Loans
    public List<Loan> getLoans() {
        return loanService.getAllLoans();
    }

    public Loan getLoanById(Long id) {
        return loanService.getLoanById(id);
    }

    public List<Loan> getLoansByUser(Long userId) {
        return loanService.getLoansByUser(userId);
    }

    public List<Loan> getLoansByBook(Long bookId) {
        return loanService.getLoansByBook(bookId);
    }

    // -- Autoren
    public List<Author> getAllAuthors() {
        return authorService.getAllAuthors();
    }

    public Author getAuthorById(Long id) {
        return authorService.getAuthorById(id);
    }

    public List<Author> getAuthorsByName(String name) {
        return authorService.getAuthorsByName(name);
    }

    // -- Genres
    public List<Genre> getAllGenres() {
        return genreService.getAllGenres();
    }

    public Genre getGenreById(Long id) {
        return genreService.getGenreById(id);
    }

    public Genre getGenresByName(String name) {
        return genreService.getGenresByName(name);
    }

    // -- Reservationen
    public List<Reservation> getAllReservations() {
        return reservationService.getAllReservations();
    }

    public Reservation getReservationById(Long id) {
        return reservationService.getReservationById(id);
    }

    public List<Reservation> getReservationsByUser(Long userId) {
        return reservationService.getReservationsByUser(userId);
    }

    public List<Reservation> getReservationByBook(Long bookId) {
        return reservationService.getReservationsByBook(bookId);
    }

    // ----------------------------------------------------------------------------
    // MUTATIONS
    // ----------------------------------------------------------------------------

    // -- Bücher
    public Book addBook(Book book) {
        return bookService.addBook(book);
    }

    public boolean deleteBook(Long id) {
        bookService.deleteBook(id);
        return true;
    }

    public boolean updateBook(Book book) {
        bookService.updateBook(book);
        return true;
    }

    // -- Ausleihen
    public Loan addLoan(Book book, User user) throws Exception {
        return loanService.addLoan(book, user);
    }

    public boolean deleteLoan(Long id) throws Exception {
        loanService.deleteLoan(id);
        return true;
    }

    public boolean updateLoan(Loan loan) {
        loanService.updateLoan(loan);
        return true;
    }

    // -- Reservationen
    public Reservation addReservation(Book book, User user) throws Exception {
        return reservationService.reserveBook(book, user);
    }

    public boolean deleteReservation(Long id) throws Exception {
        reservationService.cancelReservation(id);
        return true;
    }

    public boolean updateReservation(Reservation reservation) {
        reservationService.updateReservation(reservation);
        return true;
    }

    // -- User
    public User addUser(String name, String email) {
        return userService.addUser(name, email);
    }

    public boolean deleteUser(Long id) {
        userService.deleteUser(id);
        return true;
    }

    public boolean updateUser(User user) {
        userService.updateUser(user);
        return true;
    }

    // -- Autor
    public Author addAuthor(String name) {
        return authorService.addAuthor(name);
    }

    public boolean deleteAuthor(Long id) {
        authorService.deleteAuthor(id);
        return true;
    }

    public boolean updateAuthor(Author author) {
        authorService.updateAuthor(author);
        return true;
    }

    // -- Genre
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

    
}
