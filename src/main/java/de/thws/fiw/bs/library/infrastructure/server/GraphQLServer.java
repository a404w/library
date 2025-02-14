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

                // GraphQL-Query verarbeiten
                String jsonResponse = GraphQLHandler.handleRequest(requestBody.toString());

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
        System.out.println("✅ GraphQL Server läuft auf Port " + port);
    }

    public static void stopServer() {
        if (server != null) {
            server.stop(0);
            System.out.println("GraphQL Server gestoppt.");
        }
    }
}
