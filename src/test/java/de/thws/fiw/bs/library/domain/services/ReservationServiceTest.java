package de.thws.fiw.bs.library.domain.services;

import de.thws.fiw.bs.library.domain.model.Book;
import de.thws.fiw.bs.library.domain.model.Reservation;
import de.thws.fiw.bs.library.domain.model.User;
import de.thws.fiw.bs.library.domain.ports.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReservationServiceTest {

    private ReservationService reservationService;
    private List<Reservation> reservationStorage;

    @BeforeEach
    void setUp() {
        reservationStorage = new ArrayList<>();
        ReservationRepository reservationRepository = new ReservationRepository() {
            @Override
            public Reservation save(Reservation reservation) {
                reservationStorage.add(reservation);
                return reservation;
            }

            @Override
            public void update(Reservation reservation) {
                delete(reservation.getId());
                reservationStorage.add(reservation);
            }

            @Override
            public void delete(Long id) {
                reservationStorage.removeIf(r -> r.getId().equals(id));
            }

            @Override
            public Reservation findById(Long id) {
                return reservationStorage.stream()
                        .filter(r -> r.getId().equals(id))
                        .findFirst()
                        .orElse(null);
            }

            @Override
            public List<Reservation> findByUserId(Long userId) {
                return reservationStorage.stream()
                        .filter(r -> r.getUser().getId().equals(userId))
                        .toList();
            }

            @Override
            public List<Reservation> findByBookId(Long bookId) {
                return reservationStorage.stream()
                        .filter(r -> r.getBook().getId().equals(bookId))
                        .toList();
            }

            @Override
            public List<Reservation> findAll() {
                return new ArrayList<>(reservationStorage);
            }
        };

        reservationService = new ReservationService(reservationRepository);
    }

    @Test
    void reserveBook_ShouldStoreReservation() throws Exception {
        Book book = new Book("Dune", "123456789", null, null, true);
        User user = new User("Alice", "alice@example.com", null);

        Reservation reservation = reservationService.reserveBook(book, user);

        assertNotNull(reservation);
        assertEquals(book, reservation.getBook());
        assertEquals(user, reservation.getUser());
        assertFalse(book.isAvailable());
    }

    @Test
    void reserveBook_ShouldThrowExceptionIfBookNotAvailable() {
        Book book = new Book("Dune", "123456789", null, null, false);
        User user = new User("Bob", "bob@example.com", null);

        Exception exception = assertThrows(Exception.class, () -> reservationService.reserveBook(book, user));
        assertEquals("Buch wurde bereits reserviert oder ausgeliehen.", exception.getMessage());
    }

    @Test
    void getReservationById_ShouldReturnCorrectReservation() throws Exception {
        Book book = new Book("The Hobbit", "987654321", null, null, true);
        User user = new User("Charlie", "charlie@example.com", null);
        Reservation reservation = reservationService.reserveBook(book, user);

        Reservation foundReservation = reservationService.getReservationById(reservation.getId());
        assertNotNull(foundReservation);
        assertEquals(reservation, foundReservation);
    }

    @Test
    void getReservationsByUser_ShouldReturnReservationsForUser() throws Exception {
        Book book1 = new Book("Book 1", "111111", null, null, true);
        Book book2 = new Book("Book 2", "222222", null, null, true);
        User user = new User("Charlie", "charlie@example.com", null);

        reservationService.reserveBook(book1, user);
        reservationService.reserveBook(book2, user);

        List<Reservation> userReservations = reservationService.getReservationsByUser(user.getId());
        assertEquals(2, userReservations.size());
    }

    @Test
    void getReservationsByBook_ShouldReturnReservationsForBook() throws Exception {
        Book book = new Book("1984", "333333", null, null, true);
        User user1 = new User("David", "david@example.com", null);
        User user2 = new User("Emma", "emma@example.com", null);

        reservationService.reserveBook(book, user1);
        reservationService.reserveBook(book, user2);

        List<Reservation> bookReservations = reservationService.getReservationsByBook(book.getId());
        assertEquals(2, bookReservations.size());
    }

    @Test
    void updateReservation_ShouldModifyExistingReservation() throws Exception {
        Book book = new Book("Hyperion", "444444", null, null, true);
        User user = new User("Frank", "frank@example.com", null);
        Reservation reservation = reservationService.reserveBook(book, user);

        reservation.setReservationDate(LocalDateTime.now().plusDays(2));
        reservationService.updateReservation(reservation);

        Reservation updatedReservation = reservationService.getReservationById(reservation.getId());
        assertEquals(reservation.getReservationDate(), updatedReservation.getReservationDate());
    }

    @Test
    void cancelReservation_ShouldRemoveReservation() throws Exception {
        Book book = new Book("Brave New World", "555555", null, null, true);
        User user = new User("George", "george@example.com", null);
        Reservation reservation = reservationService.reserveBook(book, user);

        reservationService.cancelReservation(reservation.getId());

        Reservation deletedReservation = reservationService.getReservationById(reservation.getId());
        assertNull(deletedReservation);
    }

    @Test
    void cancelReservation_ShouldThrowExceptionIfBookWasNotReserved() throws Exception {
        Book book = new Book("The Alchemist", "666666", null, null, true);
        User user = new User("Henry", "henry@example.com", null);
        Reservation reservation = reservationService.reserveBook(book, user);

        book.setAvailable(true);

        Exception exception = assertThrows(Exception.class,
                () -> reservationService.cancelReservation(reservation.getId()));
        assertEquals("Buch wurde nie Reserviert.", exception.getMessage());
    }
}
