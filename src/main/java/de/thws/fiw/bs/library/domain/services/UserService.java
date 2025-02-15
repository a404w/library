package de.thws.fiw.bs.library.domain.services;

import de.thws.fiw.bs.library.domain.model.User;
import de.thws.fiw.bs.library.domain.ports.UserRepository;
import de.thws.fiw.bs.library.infrastructure.persistence.repository.UserRepositoryImpl;

import java.util.List;

public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserService() {
        this.userRepository = new UserRepositoryImpl();
    }

    public User addUser(String name, String email) {
        return userRepository.save(new User(name, email));
    }

    public void updateUser(User user) {
        userRepository.update(user);
    }

    public User getUserById(Long id) {
        System.out.println("🔎 UserService: Suche Benutzer mit ID " + id);

        User user = userRepository.findById(id);

        if (user == null) {
            System.out.println("❌ UserService: Kein Benutzer mit ID " + id + " gefunden.");
        } else {
            System.out.println("✅ UserService: Benutzer gefunden: " + user.getName() + ", " + user.getEmail());
        }

        return user;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUser(Long id) {
        userRepository.delete(id);
    }

    public User getUserByName(String name) {
        return userRepository.findByName(name);
    }
}
