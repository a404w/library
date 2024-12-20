package de.thws.fiw.bs.library.domain.services;

import de.thws.fiw.bs.library.domain.model.Book;
import de.thws.fiw.bs.library.domain.model.User;
import de.thws.fiw.bs.library.domain.ports.BookRepository;
import de.thws.fiw.bs.library.domain.ports.UserRepository;

public class BorrowService {
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public BorrowService(BookRepository bookRepository, UserRepository userRepository) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    public void borrowBook(Long bookId, Long userId) {
        Book book = bookRepository.findById(bookId);
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!book.isAvailable()) {
            throw new IllegalStateException("Book is currently not available");
        }

        book.setAvailable(false);
        user.getBorrowedBooks().add(book);

        bookRepository.save(book);
        userRepository.save(user);
    }
}
