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
            // ---------------------------------------------------------------
            // 1) Repositories anlegen
            // ---------------------------------------------------------------
            BookRepository bookRepository = new BookRepositoryImpl();
            UserRepository userRepository = new UserRepositoryImpl();
            LoanRepository loanRepository = new LoanRepositoryImpl(bookRepository, userRepository);
            ReservationRepository reservationRepository = new ReservationRepositoryImpl();
            AuthorRepository authorRepository = new AuthorRepositoryImpl();
            GenreRepository genreRepository = new GenreRepositoryImpl();

            // ---------------------------------------------------------------
            // 2) Services anlegen
            // ---------------------------------------------------------------
            BookService bookService = new BookService();
            UserService userService = new UserService();
            LoanService loanService = new LoanService(loanRepository);
            ReservationService reservationService = new ReservationService(reservationRepository);
            AuthorService authorService = new AuthorService(authorRepository);
            GenreService genreService = new GenreService(genreRepository);

            // ---------------------------------------------------------------
            // 3) Resolver anlegen
            // ---------------------------------------------------------------
            GraphQLResolvers resolvers = new GraphQLResolvers(
                bookService,
                loanService,
                reservationService,
                genreService,
                userService,
                authorService
            );

            // ---------------------------------------------------------------
            // 4) Schema aus der Datei "schema.graphqls" laden und parsen
            // ---------------------------------------------------------------
            // Stelle sicher, dass "schema.graphqls" in deinem "resources"-Ordner liegt.
            InputStream schemaStream = GraphQLServlet.class
                    .getResourceAsStream("/schema.graphqls");
            if (schemaStream == null) {
                throw new RuntimeException("❌ Die Datei 'schema.graphqls' wurde nicht gefunden!");
            }

            // Parser bauen und Resolver registrieren
            GraphQLSchema graphQLSchema = SchemaParser.newParser()
                    .file("schema.graphqls")
                    .resolvers(resolvers)
                    .build()
                    .makeExecutableSchema();

            // ---------------------------------------------------------------
            // 5) GraphQLConfiguration erstellen
            // ---------------------------------------------------------------
            configuration = GraphQLConfiguration
                    .with(graphQLSchema)
                    .build();

        } catch (Exception e) {
            throw new RuntimeException("Fehler beim Initialisieren des GraphQL-Servlets", e);
        }
    }

    @Override
    protected GraphQLConfiguration getConfiguration() {
        // Hier wird das vorab erstellte Schema (und Resolvers) an das Framework übergeben.
        return configuration;
    }

    // KEIN doPost() oder doGet() überschreiben – das macht GraphQLHttpServlet automatisch.
}
