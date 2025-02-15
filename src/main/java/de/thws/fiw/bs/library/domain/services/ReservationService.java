package de.thws.fiw.bs.library.domain.services;

import de.thws.fiw.bs.library.domain.model.Reservation;
import de.thws.fiw.bs.library.domain.model.Book;
import de.thws.fiw.bs.library.domain.model.User;
import de.thws.fiw.bs.library.domain.ports.ReservationRepository;

import java.time.LocalDateTime;
import java.util.List;

public class ReservationService {

    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public Reservation reserveBook(Book book, User user) throws Exception {
        if (!book.isAvailable()) {
            throw new Exception("Buch wurde bereits reserviert oder ausgeliehen.");
        }
        book.setAvailable(false);
        return reservationRepository.save(new Reservation(user, book, LocalDateTime.now()));
    }

    public void cancelReservation(Long reservationId) throws Exception {
        if (getReservationById(reservationId).getBook().isAvailable()) {
            throw new Exception("Buch wurde nie Reserviert.");
        }
        getReservationById(reservationId).getBook().setAvailable(true);
        reservationRepository.delete(reservationId);
    }

    public void updateReservation(Reservation reservation) {
        reservationRepository.update(reservation);
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
