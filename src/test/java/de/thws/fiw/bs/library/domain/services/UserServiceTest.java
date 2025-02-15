package de.thws.fiw.bs.library.domain.services;

import de.thws.fiw.bs.library.domain.model.User;
import de.thws.fiw.bs.library.domain.ports.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    private UserService userService;
    private List<User> users; // Direkte Speicherung ohne Repository-Klasse

    @BeforeEach
    void setUp() {
        users = new ArrayList<>();
        userService = new UserService(new UserRepository() {
            @Override
            public User save(User user) {
                user.setId((long) (users.size() + 1)); // Simulierte Datenbank-ID
                users.add(user);
                return user;
            }

            @Override
            public void update(User user) {
                delete(user.getId());
                users.add(user);
            }

            @Override
            public User findById(Long id) {
                return users.stream()
                        .filter(u -> u.getId().equals(id))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("User not found"));
            }

            @Override
            public List<User> findAll() {
                return new ArrayList<>(users);
            }

            @Override
            public void delete(Long id) {
                users.removeIf(u -> u.getId().equals(id));
            }

            @Override
            public User findByName(String name) {
                return users.stream()
                        .filter(u -> u.getName().equalsIgnoreCase(name))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("User not found"));
            }
        });
    }

    @Test
    void addUser_ShouldSaveUser() {
        User user = userService.addUser("Alice", "alice@example.com");
        assertNotNull(user.getId()); // Die ID sollte gesetzt sein
        assertEquals("Alice", user.getName());
    }

    @Test
    void getUserById_ShouldReturnCorrectUser() {
        User savedUser = userService.addUser("Bob", "bob@example.com");
        User retrievedUser = userService.getUserById(savedUser.getId());

        assertEquals(savedUser.getId(), retrievedUser.getId());
        assertEquals("Bob", retrievedUser.getName());
    }

    @Test
    void getAllUsers_ShouldReturnAllSavedUsers() {
        userService.addUser("Charlie", "charlie@example.com");
        userService.addUser("David", "david@example.com");

        List<User> usersList = userService.getAllUsers();
        assertEquals(2, usersList.size());
    }

    @Test
    void deleteUser_ShouldRemoveUser() {
        User savedUser = userService.addUser("Eve", "eve@example.com");
        userService.deleteUser(savedUser.getId());

        List<User> usersList = userService.getAllUsers();
        assertEquals(0, usersList.size());
    }

    @Test
    void getUserByName_ShouldReturnMatchingUser() {
        userService.addUser("Frank", "frank@example.com");
        userService.addUser("George", "george@example.com");

        User user = userService.getUserByName("Frank");
        assertEquals("Frank", user.getName());
    }

    @Test
    void updateUser_ShouldModifyExistingUser() {
        User savedUser = userService.addUser("Old Name", "old@example.com");
        savedUser.setName("New Name");
        savedUser.setEmail("new@example.com");
        userService.updateUser(savedUser);

        User updatedUser = userService.getUserById(savedUser.getId());
        assertEquals("New Name", updatedUser.getName());
        assertEquals("new@example.com", updatedUser.getEmail());
    }
}
