package de.thws.fiw.bs.library.domain.ports;

import de.thws.fiw.bs.library.domain.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User save(User user); // Speichert einen Nutzer

    void delete(Long id); // Löscht einen Nutzer

    Optional<User> findById(Long id); // Sucht einen Nutzer anhand der ID

    List<User> findAll(); // Gibt alle Nutzer zurück
}
