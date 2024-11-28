package de.thws.fiw.bs.library.domain.services;

import de.thws.fiw.bs.library.domain.model.Book;
import de.thws.fiw.bs.library.domain.model.User;
import de.thws.fiw.bs.library.domain.ports.BookRepository;
import de.thws.fiw.bs.library.domain.ports.UserRepository;

public class ReturnService {
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public ReturnService(BookRepository bookRepository, UserRepository userRepository) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    // Buch zurückgeben
    public void returnBook(Long bookId, Long userId) {
        Book book = bookRepository.findById(bookId);
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!user.getBorrowedBooks().contains(book)) {
            throw new IllegalStateException("Book not borrowed by this user");
        }

        book.setAvailable(true);
        user.removeBorrowedBook(book);

        bookRepository.save(book);
        userRepository.save(user);
    }
}