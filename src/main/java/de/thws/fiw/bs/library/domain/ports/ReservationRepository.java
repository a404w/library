package de.thws.fiw.bs.library.domain.ports;

import java.util.List;

import de.thws.fiw.bs.library.domain.model.Reservation;

public interface ReservationRepository {
    Reservation save(Reservation reservation); // Reservierung speichern

    void delete(Long id); // Reservierung löschen

    List<Reservation> findByUserId(Long userId); // Reservierungen eines Nutzers finden
}
