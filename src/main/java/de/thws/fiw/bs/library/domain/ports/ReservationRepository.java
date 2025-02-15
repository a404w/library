package de.thws.fiw.bs.library.domain.ports;

import de.thws.fiw.bs.library.domain.model.Reservation;

import java.util.List;

public interface ReservationRepository {
    Reservation save(Reservation reservation); 

    void update(Reservation reservation);

    void delete(Long id); 

    Reservation findById(Long id);

    List<Reservation> findByUserId(Long userId); 

    List<Reservation> findByBookId(Long bookId); 

    List<Reservation> findAll();
}
