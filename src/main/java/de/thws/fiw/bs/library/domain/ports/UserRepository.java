package de.thws.fiw.bs.library.domain.ports;

import de.thws.fiw.bs.library.domain.model.User;

import java.util.List;

public interface UserRepository {
    User save(User user);

    void delete(Long id);

    void update(User user);

    User findById(Long id);

    List<User> findAll();

    User findByName(String name);
}
