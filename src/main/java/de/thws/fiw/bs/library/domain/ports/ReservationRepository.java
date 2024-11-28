package de.thws.fiw.bs.library.domain.ports;

import java.util.List;

import de.thws.fiw.bs.library.domain.model.Reservation;

public interface ReservationRepository {
    Reservation save(Reservation reservation);

    void delete(Long id);

    List<Reservation> findByUserId(Long userId);
}
