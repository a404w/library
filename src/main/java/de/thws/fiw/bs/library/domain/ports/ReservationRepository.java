package de.thws.fiw.bs.library.domain.ports;

import de.thws.fiw.bs.library.domain.model.Genre;
import de.thws.fiw.bs.library.domain.model.Reservation;
import de.thws.fiw.bs.library.domain.model.User;

import java.util.List;

public interface ReservationRepository {
    Reservation save(Reservation reservation); // Speichert eine neue Reservierung

    void update(Reservation reservation);

    void delete(Long id); // Löscht eine Reservierung nach ID

    Reservation findById(Long id);

    List<Reservation> findByUserId(Long userId); // Findet alle Reservierungen eines Nutzers

    List<Reservation> findByBookId(Long bookId); // Findet alle Reservierungen eines Buchs

    List<Reservation> findAll();
}
