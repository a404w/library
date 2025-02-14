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
        if (book == null) {
            throw new IllegalArgumentException("Book not found");
        }

        User user = userRepository.findById(userId);

        if (!book.isAvailable()) {
            throw new IllegalStateException("Book is currently not available");
        }

        if (user.getBorrowedBooks().contains(book)) {
            throw new IllegalStateException("User has already borrowed this book");
        }

        book.setAvailable(false);
        user.getBorrowedBooks().add(book);

        bookRepository.save(book);
        userRepository.save(user);
    }

}
