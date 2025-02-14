package de.thws.fiw.bs.library.domain.services;

import de.thws.fiw.bs.library.domain.model.Book;
import de.thws.fiw.bs.library.domain.model.Reservation;
import de.thws.fiw.bs.library.domain.model.User;
import de.thws.fiw.bs.library.domain.ports.BookRepository;
import de.thws.fiw.bs.library.domain.ports.ReservationRepository;
import de.thws.fiw.bs.library.domain.ports.UserRepository;

import java.time.LocalDateTime;

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

    public void reserveBook(Long bookId, Long userId) {
        Book book = bookRepository.findById(bookId);
        User user = userRepository.findById(userId);

        if (book.isAvailable()) {
            throw new IllegalStateException("Book is available, no reservation needed");
        }

        Reservation reservation = new Reservation(null, user, book, LocalDateTime.now());
        reservationRepository.save(reservation);
    }
}
