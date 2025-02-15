package de.thws.fiw.bs.library.domain.services;

import de.thws.fiw.bs.library.domain.model.Author;
import de.thws.fiw.bs.library.domain.ports.AuthorRepository;
import java.util.List;

public class AuthorService {
    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public Author addAuthor(String name) {
        return authorRepository.save(new Author(name));
    }

    public Author getAuthorById(Long id) {
        return authorRepository.findById(id);
    }

    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    public void deleteAuthor(Long id) {
        authorRepository.delete(id);
    }

    public List<Author> getAuthorsByName(String name) {
        return authorRepository.getAuthorsByName(name);
    }

    public void updateAuthor(Author author) {
        authorRepository.update(author); 
    }
}
