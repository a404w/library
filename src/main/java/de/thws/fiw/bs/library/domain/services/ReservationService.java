package de.thws.fiw.bs.library.domain.services;

import java.time.LocalDateTime;

import de.thws.fiw.bs.library.domain.model.Book;
import de.thws.fiw.bs.library.domain.model.Reservation;
import de.thws.fiw.bs.library.domain.model.User;
import de.thws.fiw.bs.library.domain.ports.BookRepository;
import de.thws.fiw.bs.library.domain.ports.ReservationRepository;
import de.thws.fiw.bs.library.domain.ports.UserRepository;

public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public ReservationService(ReservationRepository reservationRepository, BookRepository bookRepository,
            UserRepository userRepository) {
        this.reservationRepository = reservationRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    // Buch reservieren
    public void reserveBook(Long bookId, Long userId) {
        Book book = bookRepository.findById(bookId);
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (book.isAvailable()) {
            throw new IllegalStateException("Book is available, no reservation needed");
        }

        Reservation reservation = new Reservation(null, user, book, LocalDateTime.now());
        reservationRepository.save(reservation);
    }
}
