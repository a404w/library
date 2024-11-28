package de.thws.fiw.bs.library.domain.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import de.thws.fiw.bs.library.domain.model.User;
import de.thws.fiw.bs.library.domain.ports.UserRepository;

class MockUserRepository implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public User save(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public void delete(Long id) {
        users.remove(id);
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }
}
