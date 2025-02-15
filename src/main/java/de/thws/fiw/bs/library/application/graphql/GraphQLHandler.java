/*package de.thws.fiw.bs.library.application.graphql;

import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.*;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

import de.thws.fiw.bs.library.domain.ports.BookRepository;
import de.thws.fiw.bs.library.domain.ports.LoanRepository;
import de.thws.fiw.bs.library.domain.ports.UserRepository;
import de.thws.fiw.bs.library.domain.services.*;
import de.thws.fiw.bs.library.infrastructure.persistence.repository.AuthorRepositoryImpl;
import de.thws.fiw.bs.library.infrastructure.persistence.repository.BookRepositoryImpl;
import de.thws.fiw.bs.library.infrastructure.persistence.repository.GenreRepositoryImpl;
import de.thws.fiw.bs.library.infrastructure.persistence.repository.LoanRepositoryImpl;
import de.thws.fiw.bs.library.infrastructure.persistence.repository.ReservationRepositoryImpl;
import de.thws.fiw.bs.library.infrastructure.persistence.repository.UserRepositoryImpl;

public class GraphQLHandler {
    private static GraphQL graphQL;

    static {
        try {
            // Services initialisieren
            BookRepository bookRepository = new BookRepositoryImpl();
            UserRepository userRepository = new UserRepositoryImpl();
            LoanRepository loanRepository = new LoanRepositoryImpl(bookRepository, userRepository);
            LoanService loanService = new LoanService(loanRepository);
            BookService bookService = new BookService();
            ReservationService reservationService = new ReservationService(new ReservationRepositoryImpl());
            UserService userService = new UserService();
            AuthorService authorService = new AuthorService(new AuthorRepositoryImpl());
            GenreService genreService = new GenreService(new GenreRepositoryImpl());

            // Resolver mit Services verbinden
            GraphQLResolvers resolvers = new GraphQLResolvers(
                    bookService, loanService, reservationService, genreService, userService, authorService);

            // Schema laden
            InputStream schemaStream = GraphQLHandler.class.getClassLoader().getResourceAsStream("schema.graphqls");
            if (schemaStream == null) {
                throw new RuntimeException("❌ Fehler: Schema-Datei `schema.graphqls` nicht gefunden! Stelle sicher, dass sie in `src/main/resources/` liegt.");
            }
            Reader schemaReader = new InputStreamReader(schemaStream, StandardCharsets.UTF_8);

            // GraphQL-Schema parsen
            TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(schemaReader);
            RuntimeWiring wiring = RuntimeWiring.newRuntimeWiring()
                    // Queries
                    .type(TypeRuntimeWiring.newTypeWiring("Query")
                            .dataFetcher("books", env -> resolvers.getBooks())
                            .dataFetcher("bookById", env -> resolvers.getBookById(env.getArgument("id")))
                            .dataFetcher("booksByGenre", env -> resolvers.getBooksByGenreId(env.getArgument("id")))
                            .dataFetcher("booksByAuthor", env -> resolvers.getBooksByAuthorId(env.getArgument("id")))
                            .dataFetcher("users", env -> resolvers.getUsers())
                            .dataFetcher("userById", env -> resolvers.getUserById(env.getArgument("id")))
                            .dataFetcher("userByName", env -> resolvers.getUserByName(env.getArgument("name")))
                            .dataFetcher("loans", env -> resolvers.getLoans())
                            .dataFetcher("loanById", env -> resolvers.getLoanById(env.getArgument("id")))
                            .dataFetcher("loansByUser", env -> resolvers.getLoansByUser(env.getArgument("id")))
                            .dataFetcher("authors", env -> resolvers.getAllAuthors())
                            .dataFetcher("authorById", env -> resolvers.getAuthorById(env.getArgument("id")))
                            .dataFetcher("authorsByName", env -> resolvers.getAuthorsByName(env.getArgument("name")))
                            .dataFetcher("genres", env -> resolvers.getAllGenres())
                            .dataFetcher("genreById", env -> resolvers.getGenreById(env.getArgument("id")))
                            .dataFetcher("genresByName", env -> resolvers.getGenresByName(env.getArgument("name")))
                    )
                    // Mutations
                    .type(TypeRuntimeWiring.newTypeWiring("Mutation")
                            .dataFetcher("addBook", env -> resolvers.addBook(env.getArgument("book")))
                            .dataFetcher("deleteBook", env -> resolvers.deleteBook(env.getArgument("id")))
                            .dataFetcher("updateBook", env -> resolvers.updateBook(env.getArgument("book")))
                            .dataFetcher("addLoan", env -> resolvers.addLoan(env.getArgument("book"), env.getArgument("user")))
                            .dataFetcher("deleteLoan", env -> resolvers.deleteLoan(env.getArgument("id")))
                            .dataFetcher("updateLoan", env -> resolvers.updateLoan(env.getArgument("loan")))
                            .dataFetcher("addUser", env -> resolvers.addUser(env.getArgument("name"), env.getArgument("email")))
                            .dataFetcher("deleteUser", env -> resolvers.deleteUser(env.getArgument("id")))
                            .dataFetcher("updateUser", env -> resolvers.updateUser(env.getArgument("user")))
                            .dataFetcher("addAuthor", env -> resolvers.addAuthor(env.getArgument("name")))
                            .dataFetcher("deleteAuthor", env -> resolvers.deleteAuthor(env.getArgument("id")))
                            .dataFetcher("updateAuthor", env -> resolvers.updateAuthor(env.getArgument("author")))
                            .dataFetcher("addGenre", env -> resolvers.addGenre(env.getArgument("genrename"), env.getArgument("beschreibung")))
                            .dataFetcher("deleteGenre", env -> resolvers.deleteGenre(env.getArgument("id")))
                            .dataFetcher("updateGenre", env -> resolvers.updateGenre(env.getArgument("genre")))
                    )
                    .build();

            GraphQLSchema schema = new SchemaGenerator().makeExecutableSchema(typeRegistry, wiring);
            graphQL = GraphQL.newGraphQL(schema).build();
        } catch (Exception e) {
            throw new RuntimeException("❌ Fehler beim Initialisieren des GraphQL-Schemas", e);
        }
    }
    public static GraphQL getGraphQL() {
        return graphQL;
    }
    public static String handleRequest(String query) {
        System.out.println("📥 Empfange GraphQL Anfrage: " + query);
        
        // Anfrage an GraphQL senden
        String response = graphQL.execute(query).toSpecification().toString();
    
        // Debugging: Antwort ausgeben
        System.out.println("📤 Antwort von GraphQL: " + response);
    
        return response;
    }
    
    public static GraphQLSchema getGraphQLSchema() {
        return graphQL.getGraphQLSchema();
    }
    
}
*/