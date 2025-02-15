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
        return userRepository.findById(id); 
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
