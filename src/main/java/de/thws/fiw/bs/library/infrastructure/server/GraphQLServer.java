package de.thws.fiw.bs.library.infrastructure.server;

import com.sun.net.httpserver.HttpServer;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class GraphQLServer {
    private static HttpServer server;

    public static void main(String[] args) throws IOException {
        int port = 8080;
        server = HttpServer.create(new InetSocketAddress(port), 0);

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

    private static String executeGraphQLQuery(String requestBody) {
        System.out.println("GraphQL Request: " + requestBody); // Debugging-Ausgabe

        if (requestBody.contains("getBooks")) {
            return "{ \"data\": { \"getBooks\": [{ \"id\": 1, \"title\": \"Book 1\", \"isbn\": \"12345\" }] } }";
        } else if (requestBody.contains("getUsers")) {
            return "{ \"data\": { \"getUsers\": [{ \"id\": 1, \"name\": \"Test User\", \"email\": \"test@example.com\" }] } }";
        } else if (requestBody.contains("addBook")) {
            return "{ \"data\": { \"addBook\": { \"id\": 1, \"title\": \"Test Book\" } } }";
        }

        return "{ \"data\": {} }";
    }

    public static void stopServer() {
        if (server != null) {
            server.stop(0);
            System.out.println("GraphQL Server gestoppt.");
        }
    }
}
