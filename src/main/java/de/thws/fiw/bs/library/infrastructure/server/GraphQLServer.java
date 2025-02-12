package de.thws.fiw.bs.library.infrastructure.server;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class GraphQLServer {
    public static void main(String[] args) throws IOException {
        int port = 8080;
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        server.createContext("/graphql", exchange -> {
            if ("POST".equals(exchange.getRequestMethod())) {
                String response = "{\"data\": \"GraphQL Server läuft!\"}";
                exchange.sendResponseHeaders(200, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } else {
                exchange.sendResponseHeaders(405, -1); // Method Not Allowed
            }
        });

        server.setExecutor(null); // Default executor
        server.start();
        System.out.println("GraphQL Server läuft auf Port " + port);
    }
}
