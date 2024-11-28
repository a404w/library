package de.thws.fiw.bs.library.domain.ports;

import java.util.Optional;

import de.thws.fiw.bs.library.domain.model.User;

public interface UserRepository {
    Optional<User> findById(Long id);

    void save(User user);

    void delete(Long id);
}
