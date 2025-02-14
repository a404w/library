package de.thws.fiw.bs.library.domain.services;

import de.thws.fiw.bs.library.domain.model.Reservation;
import de.thws.fiw.bs.library.domain.model.Book;
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
    private List<Reservation> reservations; // Direkte Speicherung ohne Repository-Klasse

    @BeforeEach
    void setUp() {
        reservations = new ArrayList<>();
        reservationService = new ReservationService(new ReservationRepository() {
            @Override
            public Reservation save(Reservation reservation) {
                reservation.setId((long) (reservations.size() + 1)); // Simulierte Datenbank-ID
                reservations.add(reservation);
                return reservation;
            }

            @Override
            public void delete(Long id) {
                reservations.removeIf(r -> r.getId().equals(id));
            }

            @Override
            public void update(Reservation reservation) {
                delete(reservation.getId());
                reservations.add(reservation);
            }

            @Override
            public Reservation findById(Long id) {
                return reservations.stream()
                        .filter(r -> r.getId().equals(id))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Reservation not found"));
            }

            @Override
            public List<Reservation> findByUserId(Long userId) {
                List<Reservation> result = new ArrayList<>();
                for (Reservation r : reservations) {
                    if (r.getUser().getId().equals(userId)) {
                        result.add(r);
                    }
                }
                return result;
            }

            @Override
            public List<Reservation> findByBookId(Long bookId) {
                List<Reservation> result = new ArrayList<>();
                for (Reservation r : reservations) {
                    if (r.getBook().getId().equals(bookId)) {
                        result.add(r);
                    }
                }
                return result;
            }

            @Override
            public List<Reservation> findAll() {
                return new ArrayList<>(reservations);
            }
        });
    }

    @Test
    void reserveBook_ShouldSaveReservation() {
        Book book = new Book("1984", "123456789", null, null, true);
        User user = new User("John Doe", "john@example.com", null);

        Reservation reservation = reservationService.reserveBook(book, user);
        assertNotNull(reservation.getId()); // Die ID sollte gesetzt sein
        assertEquals(book, reservation.getBook());
        assertEquals(user, reservation.getUser());
    }

    @Test
    void getReservationById_ShouldReturnCorrectReservation() {
        Book book = new Book("Brave New World", "987654321", null, null, true);
        User user = new User("Jane Doe", "jane@example.com", null);

        Reservation savedReservation = reservationService.reserveBook(book, user);
        Reservation retrievedReservation = reservationService.getReservationById(savedReservation.getId());

        assertEquals(savedReservation.getId(), retrievedReservation.getId());
        assertEquals(book, retrievedReservation.getBook());
        assertEquals(user, retrievedReservation.getUser());
    }

    @Test
    void getAllReservations_ShouldReturnAllSavedReservations() {
        Book book1 = new Book("Dune", "111111111", null, null, true);
        User user1 = new User("Alice", "alice@example.com", null);
        reservationService.reserveBook(book1, user1);

        Book book2 = new Book("The Hobbit", "222222222", null, null, true);
        User user2 = new User("Bob", "bob@example.com", null);
        reservationService.reserveBook(book2, user2);

        List<Reservation> reservationsList = reservationService.getAllReservations();
        assertEquals(2, reservationsList.size());
    }

    @Test
    void cancelReservation_ShouldRemoveReservation() {
        Book book = new Book("Lord of the Rings", "333333333", null, null, true);
        User user = new User("Charlie", "charlie@example.com", null);
        Reservation savedReservation = reservationService.reserveBook(book, user);

        reservationService.cancelReservation(savedReservation.getId());

        List<Reservation> reservationsList = reservationService.getAllReservations();
        assertEquals(0, reservationsList.size());
    }

    @Test
    void getReservationsByUser_ShouldReturnMatchingReservations() {
        User user = new User("David", "david@example.com", null);
        Book book1 = new Book("Neuromancer", "444444444", null, null, true);
        Book book2 = new Book("Snow Crash", "555555555", null, null, true);

        reservationService.reserveBook(book1, user);
        reservationService.reserveBook(book2, user);

        List<Reservation> userReservations = reservationService.getReservationsByUser(user.getId());
        assertEquals(2, userReservations.size());
    }

    @Test
    void getReservationsByBook_ShouldReturnMatchingReservations() {
        User user1 = new User("Emma", "emma@example.com", null);
        User user2 = new User("Frank", "frank@example.com", null);
        Book book = new Book("Foundation", "666666666", null, null, true);

        reservationService.reserveBook(book, user1);
        reservationService.reserveBook(book, user2);

        List<Reservation> bookReservations = reservationService.getReservationsByBook(book.getId());
        assertEquals(2, bookReservations.size());
    }

    @Test
    void updateReservation_ShouldModifyExistingReservation() {
        Book book = new Book("Hyperion", "777777777", null, null, true);
        User user = new User("Grace", "grace@example.com", null);
        Reservation savedReservation = reservationService.reserveBook(book, user);

        LocalDateTime newReservationDate = LocalDateTime.now().plusDays(7);
        savedReservation.setReservationDate(newReservationDate);
        reservationService.updateReservation(savedReservation);

        Reservation updatedReservation = reservationService.getReservationById(savedReservation.getId());
        assertEquals(newReservationDate, updatedReservation.getReservationDate());
    }
}
