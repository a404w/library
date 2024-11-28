package de.thws.fiw.bs.library.domain.services;

import de.thws.fiw.bs.library.domain.model.Book;
import de.thws.fiw.bs.library.domain.model.User;

public class BorrowService {

    public void borrowBook(User user, Book book) {
        if (!book.isAvailable()) {
            throw new IllegalStateException("Book is not available for borrowing.");
        }
        book.setAvailable(false);
        user.addBorrowedBook(book);
    }
}
