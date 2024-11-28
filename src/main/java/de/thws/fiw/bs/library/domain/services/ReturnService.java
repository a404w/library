package de.thws.fiw.bs.library.domain.services;

import de.thws.fiw.bs.library.domain.model.Book;
import de.thws.fiw.bs.library.domain.model.User;

public class ReturnService {

    public void returnBook(User user, Book book) {
        if (!user.getBorrowedBooks().contains(book)) {
            throw new IllegalStateException("User has not borrowed this book.");
        }
        book.setAvailable(true);
        user.removeBorrowedBook(book);
    }
}
