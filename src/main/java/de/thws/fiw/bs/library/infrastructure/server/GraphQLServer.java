package de.thws.fiw.bs.library.infrastructure.server;

import com.sun.net.httpserver.HttpServer;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.StaticDataFetcher;
import graphql.schema.idl.*;

public class GraphQLServer {
    private static GraphQL graphQL;

    public static void main(String[] args) throws IOException {
        int port = 8080;
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        // GraphQL-Schema laden
        buildGraphQLSchema();

        server.createContext("/graphql", exchange -> {
            if ("POST".equals(exchange.getRequestMethod())) {
                InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
                BufferedReader br = new BufferedReader(isr);
                StringBuilder requestBody = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    requestBody.append(line);
                }

                // GraphQL-Anfrage ausführen
                String jsonResponse = executeGraphQLQuery(requestBody.toString());

                exchange.sendResponseHeaders(200, jsonResponse.length());
                OutputStream os = exchange.getResponseBody();
                os.write(jsonResponse.getBytes());
                os.close();
            } else {
                exchange.sendResponseHeaders(405, -1); // Method Not Allowed
            }
        });

        server.setExecutor(null);
        server.start();
        System.out.println("GraphQL Server läuft auf Port " + port);
    }

    private static void buildGraphQLSchema() {
        String schema = "type Query { hello: String }";
        TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(schema);
        RuntimeWiring wiring = RuntimeWiring.newRuntimeWiring()
                .type(TypeRuntimeWiring.newTypeWiring("Query")
                        .dataFetcher("hello", new StaticDataFetcher("Hallo GraphQL!")))
                .build();
        GraphQLSchema graphQLSchema = new SchemaGenerator().makeExecutableSchema(typeRegistry, wiring);
        graphQL = GraphQL.newGraphQL(graphQLSchema).build();
    }

    private static String executeGraphQLQuery(String query) {
        return "{ \"data\": { \"hello\": \"Hallo GraphQL!\" } }";
    }
}
