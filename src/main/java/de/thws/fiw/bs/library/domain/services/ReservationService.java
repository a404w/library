package de.thws.fiw.bs.library.domain.services;

import de.thws.fiw.bs.library.domain.model.Reservation;
import de.thws.fiw.bs.library.domain.model.Book;
import de.thws.fiw.bs.library.domain.model.User;
import de.thws.fiw.bs.library.domain.ports.ReservationRepository;
import de.thws.fiw.bs.library.infrastructure.persistence.repository.BookRepositoryImpl;
import de.thws.fiw.bs.library.infrastructure.persistence.repository.UserRepositoryImpl;

import java.time.LocalDateTime;
import java.util.List;

public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final BookRepositoryImpl bookRepository;
    private final UserRepositoryImpl userRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
        this.bookRepository = new BookRepositoryImpl();
        this.userRepository = new UserRepositoryImpl();
    }

    public Reservation reserveBook(Long bookId, Long userId) throws Exception {
        Book book = bookRepository.findById(bookId);
        User user = userRepository.findById(userId);

        if (book == null || user == null) {
            throw new Exception("❌ Fehler: Buch oder Benutzer existiert nicht.");
        }

        if (!book.isAvailable()) {
            throw new Exception("❌ Fehler: Das Buch ist bereits ausgeliehen oder reserviert.");
        }

        book.setAvailable(false);
        bookRepository.update(book);

        Reservation reservation = new Reservation(user, book, LocalDateTime.now());
        return reservationRepository.save(reservation);
    }

    public void cancelReservation(Long reservationId) throws Exception {
        Reservation reservation = getReservationById(reservationId);

        if (reservation == null) {
            throw new Exception("❌ Fehler: Reservierung existiert nicht.");
        }

        Book book = reservation.getBook();
        book.setAvailable(true);
        bookRepository.update(book);

        reservationRepository.delete(reservationId);
    }

    public boolean updateReservation(Reservation reservation) {
        System.out.println("🔄 Überprüfung der Reservierung mit ID: " + reservation.getId());

        Reservation existingReservation = reservationRepository.findById(reservation.getId());
        if (existingReservation == null) {
            System.err.println("❌ Fehler: Reservierung mit ID " + reservation.getId() + " existiert nicht.");
            throw new RuntimeException("Reservierung mit ID " + reservation.getId() + " existiert nicht.");
        }

        System.out.println("🔄 Aktualisiere Reservierung...");
        reservationRepository.update(reservation);
        System.out.println("✅ Reservierung erfolgreich aktualisiert.");
        return true;
    }

    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id);
    }

    public List<Reservation> getReservationsByUser(Long userId) {
        return reservationRepository.findByUserId(userId);
    }

    public List<Reservation> getReservationsByBook(Long bookId) {
        return reservationRepository.findByBookId(bookId);
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }
}
