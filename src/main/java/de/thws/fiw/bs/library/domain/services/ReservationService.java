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

    public Reservation reserveBook(Book book, User user) {
        return reservationRepository.save(new Reservation(user, book, LocalDateTime.now())); // Reservierung speichern
    }

    public void cancelReservation(Long reservationId) {
        reservationRepository.delete(reservationId); // Reservierung stornieren
    }

    public void updateReservation(Reservation reservation){
        reservationRepository.update(reservation);
    }

    public Reservation getReservationById(Long id){
        return reservationRepository.findById(id);
    }

    public List<Reservation> getReservationsByUser(Long userId) {
        return reservationRepository.findByUserId(userId); // Alle Reservierungen eines Nutzers
    }

    public List<Reservation> getReservationsByBook(Long bookId) {
        return reservationRepository.findByBookId(bookId); // Alle Reservierungen eines Buches
    }
}
