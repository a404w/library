package de.thws.fiw.bs.library.application.graphql;

import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.*;

import java.io.Reader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import de.thws.fiw.bs.library.domain.services.*;


public class GraphQLHandler {
    private static GraphQL graphQL;

    static {
        try {
            // Services initialisieren
            BookService bookService = new BookService();
            UserService userService = new UserService();

            // Resolver mit Services verbinden
            GraphQLResolvers resolvers = new GraphQLResolvers(bookService,userService);

            // Schema laden
            InputStream schemaStream = GraphQLHandler.class.getClassLoader().getResourceAsStream("schema.graphqls");
            if (schemaStream == null) throw new RuntimeException("Schema-Datei nicht gefunden!");
            Reader schemaReader = new InputStreamReader(schemaStream, StandardCharsets.UTF_8);

            // GraphQL-Schema parsen
            TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(schemaReader);
            RuntimeWiring wiring = RuntimeWiring.newRuntimeWiring()
                    .type(TypeRuntimeWiring.newTypeWiring("Query")
                            .dataFetcher("books", env -> resolvers.getBooks())
                            .dataFetcher("bookById", env -> resolvers.getBookById(env.getArgument("id")))
                            .dataFetcher("booksByGenre", env -> resolvers.getBooksByGenre(env.getArgument("genre"))))
                    .type(TypeRuntimeWiring.newTypeWiring("Mutation")
                            .dataFetcher("addBook", env -> resolvers.addBook(
                                    env.getArgument("id"),
                                    env.getArgument("title"),
                                    env.getArgument("isbn"),
                                    env.getArgument("genre"),
                                    env.getArgument("authors"),
                                    env.getArgument("isAvailable")
                            ))
                            .dataFetcher("deleteBook", env -> {
                                resolvers.deleteBook(env.getArgument("id"));
                                return true;
                            }))
                    .build();

            GraphQLSchema schema = new SchemaGenerator().makeExecutableSchema(typeRegistry, wiring);
            graphQL = GraphQL.newGraphQL(schema).build();
        } catch (Exception e) {
            throw new RuntimeException("❌ Fehler beim Initialisieren des GraphQL-Schemas", e);
        }
    }

    public static String handleRequest(String query) {
        return graphQL.execute(query).toSpecification().toString();
    }
}
