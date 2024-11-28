package de.thws.fiw.bs.library.domain.services;

import java.util.Optional;

import de.thws.fiw.bs.library.domain.model.User;
import de.thws.fiw.bs.library.domain.ports.UserRepository;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Nutzer registrieren
    public User registerUser(User user) {
        return userRepository.save(user);
    }

    // Nutzer löschen
    public void deleteUser(Long userId) {
        userRepository.delete(userId);
    }

    // Nutzer finden
    public Optional<User> findUserById(Long userId) {
        return userRepository.findById(userId);
    }
}
