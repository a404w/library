package de.thws.fiw.bs.library.domain.ports;

import java.util.Optional;

import de.thws.fiw.bs.library.domain.model.User;

public interface UserRepository {
    User save(User user); // Nutzer speichern

    void delete(Long id); // Nutzer löschen

    Optional<User> findById(Long id); // Nutzer nach ID suchen
}
