// package de.thws.fiw.bs.library.domain.services;

// import de.thws.fiw.bs.library.domain.model.Book;
// import de.thws.fiw.bs.library.domain.model.User;
// import de.thws.fiw.bs.library.domain.ports.BookRepository;
// import de.thws.fiw.bs.library.domain.ports.UserRepository;

// public class ReturnService {
// private final BookRepository bookRepository;
// private final UserRepository userRepository;

// public ReturnService(BookRepository bookRepository, UserRepository
// userRepository) {
// this.bookRepository = bookRepository;
// this.userRepository = userRepository;
// }

// public void returnBook(Long bookId, Long userId) {
// Book book = bookRepository.findById(bookId);
// User user = userRepository.findById(userId);

// if (!user.getBorrowedBooks().contains(book)) {
// throw new IllegalStateException("User has not borrowed this book");
// }

// book.setAvailable(true);
// user.getBorrowedBooks().remove(book);

// bookRepository.save(book);
// userRepository.save(user);
// }
// }
