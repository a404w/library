/*package de.thws.fiw.bs.library.infrastructure.server;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

class GraphQLServerTest {

    private static final String GRAPHQL_ENDPOINT = "http://localhost:8080/graphql";
    private ExecutorService serverExecutor;

    @BeforeEach
    void setUp() throws InterruptedException {
        // Starte den Server in einem separaten Thread
        serverExecutor = Executors.newSingleThreadExecutor();
        serverExecutor.submit(() -> {
            try {
                GraphQLServer.main(new String[]{});
            } catch (IOException e) {
                throw new RuntimeException("Fehler beim Starten des GraphQL-Servers", e);
            }
        });
    
        // Warte, bis der Server gestartet ist
        Thread.sleep(2000);
    }

    @AfterEach
    void tearDown() {
        if (serverExecutor != null) {
            serverExecutor.shutdownNow(); // Server stoppen
        }
    }

    private String sendGraphQLRequest(String query) throws IOException {
        URL url = URI.create(GRAPHQL_ENDPOINT).toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = query.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
        }

        return response.toString();
    }

    @Test
    void testGetBooks() throws Exception {
        String query = "{ \"query\": \"{ getBooks { id title isbn } }\" }";
        String response = sendGraphQLRequest(query);
        //System.out.println("GraphQL Response: " + response.toString());


        assertTrue(response.contains("\"getBooks\""), "Response should contain 'getBooks'");
    }

    @Test
    void testGetUsers() throws Exception {
        String query = "{ \"query\": \"{ getUsers { id name email } }\" }";
        String response = sendGraphQLRequest(query);
        //System.out.println("GraphQL Response: " + response.toString());


        assertTrue(response.contains("\"getUsers\""), "Response should contain 'getUsers'");
    }

    @Test
    void testAddBookMutation() throws Exception {
        String mutation = "{ \"query\": \"mutation { addBook(id: 1, title: \\\"Test Book\\\", isbn: \\\"12345\\\", genres: [\\\"Fiction\\\"], authors: [], isAvailable: true) { id title } }\" }";
        String response = sendGraphQLRequest(mutation);
        //System.out.println("GraphQL Response: " + response.toString());


        assertTrue(response.contains("\"addBook\""), "Response should contain 'addBook'");
        assertTrue(response.contains("\"title\": \"Test Book\""), "Book title should be 'Test Book'");
    }
    @Test
    void testInvalidQuery() throws Exception {
        String invalidQuery = "{ \"query\": \"{ unknownQuery { id } }\" }";
        String response = sendGraphQLRequest(invalidQuery);
    
        assertFalse(response.contains("\"unknownQuery\""), "Response should not contain 'unknownQuery'");
        assertTrue(response.contains("\"data\": {}"), "Response should indicate an empty data object");
    }
    
    @Test
    void testAddBookAndRetrieveIt() throws Exception {
        // Erst ein Buch hinzufügen
        String mutation = "{ \"query\": \"mutation { addBook(id: 2, title: \\\"New Book\\\", isbn: \\\"67890\\\", genres: [\\\"Sci-Fi\\\"], authors: [], isAvailable: true) { id title } }\" }";
        String addResponse = sendGraphQLRequest(mutation);
        System.out.println("GraphQL Response: " + addResponse.toString());
        assertTrue(addResponse.contains("\"addBook\""), "Response should contain 'addBook'");
        assertTrue(addResponse.contains("\"title\": \"New Book\""), "Book title should be 'New Book'");
    
        // Jetzt nach allen Büchern fragen und prüfen, ob das Buch enthalten ist
        String query = "{ \"query\": \"{ getBooks { id title isbn } }\" }";
        String getResponse = sendGraphQLRequest(query);
    
        assertTrue(getResponse.contains("\"title\": \"New Book\""), "Added book should be retrievable");
    }
    
    @Test
    void testUnsupportedMethod() throws Exception {
        // Versuch, eine GET-Anfrage zu senden, sollte nicht erlaubt sein
        URL url = URI.create(GRAPHQL_ENDPOINT).toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        
        int responseCode = connection.getResponseCode();
        assertEquals(405, responseCode, "Server should return 405 for unsupported GET method");
    }
}
 */