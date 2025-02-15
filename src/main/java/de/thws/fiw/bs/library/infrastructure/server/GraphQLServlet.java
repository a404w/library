package de.thws.fiw.bs.library.infrastructure.server;

import graphql.kickstart.servlet.GraphQLConfiguration;
import graphql.kickstart.servlet.GraphQLHttpServlet;
import graphql.kickstart.tools.SchemaParser;
import jakarta.servlet.annotation.WebServlet;

import de.thws.fiw.bs.library.application.graphql.GraphQLResolvers;
import de.thws.fiw.bs.library.domain.ports.*;
import de.thws.fiw.bs.library.domain.services.*;
import de.thws.fiw.bs.library.infrastructure.persistence.repository.*;

import graphql.schema.GraphQLSchema;

import java.io.InputStream;

@WebServlet(urlPatterns = "/graphql")
public class GraphQLServlet extends GraphQLHttpServlet {

    private static final long serialVersionUID = 1L;
    private static GraphQLConfiguration configuration;

    static {
        try {
            
            BookRepository bookRepository = new BookRepositoryImpl();
            UserRepository userRepository = new UserRepositoryImpl();
            LoanRepository loanRepository = new LoanRepositoryImpl(bookRepository, userRepository);
            ReservationRepository reservationRepository = new ReservationRepositoryImpl();
            AuthorRepository authorRepository = new AuthorRepositoryImpl();
            GenreRepository genreRepository = new GenreRepositoryImpl();


            BookService bookService = new BookService();
            UserService userService = new UserService();
            LoanService loanService = new LoanService(loanRepository);
            ReservationService reservationService = new ReservationService(reservationRepository);
            AuthorService authorService = new AuthorService(authorRepository);
            GenreService genreService = new GenreService(genreRepository);

            
            GraphQLResolvers resolvers = new GraphQLResolvers(
                bookService,
                loanService,
                reservationService,
                genreService,
                userService,
                authorService
            );


            InputStream schemaStream = GraphQLServlet.class
                    .getResourceAsStream("/schema.graphqls");
            if (schemaStream == null) {
                throw new RuntimeException("❌ Die Datei 'schema.graphqls' wurde nicht gefunden!");
            }

            GraphQLSchema graphQLSchema = SchemaParser.newParser()
                    .file("schema.graphqls")
                    .resolvers(resolvers)
                    .build()
                    .makeExecutableSchema();

            
            configuration = GraphQLConfiguration
                    .with(graphQLSchema)
                    .build();

        } catch (Exception e) {
            throw new RuntimeException("Fehler beim Initialisieren des GraphQL-Servlets", e);
        }
    }

    @Override
    protected GraphQLConfiguration getConfiguration() {
        
        return configuration;
    }

    
}
