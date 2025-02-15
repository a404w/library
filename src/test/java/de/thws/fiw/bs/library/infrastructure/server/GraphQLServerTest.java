package de.thws.fiw.bs.library.infrastructure.server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.junit.jupiter.api.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GraphQLServerTest {

    private Server server;
    private HttpClient httpClient;

    @BeforeAll
    void setUp() throws Exception {

        server = new Server(8080);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");

        ServletHolder servletHolder = new ServletHolder(new GraphQLServlet());
        context.addServlet(servletHolder, "/graphql");

        server.setHandler(context);
        server.start();

        httpClient = HttpClient.newHttpClient();
    }

    @AfterAll
    void tearDown() throws Exception {
        if (server != null) {
            server.stop();
        }
    }

    @Test
    @DisplayName("Testet, ob der GraphQL-Server erreichbar ist")
    void testGraphQLServerRunning() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/graphql"))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(400, response.statusCode());
    }

    @Test
    @DisplayName("Testet eine einfache GraphQL-Query")
    void testGraphQLQuery() throws Exception {
        String query = "{ \"query\": \"{ getBooks { id title } }\" }";

    
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/graphql"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(query))
                .build();
    
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    
        System.out.println("Response Code: " + response.statusCode());
        System.out.println("Response Body: " + response.body());
    
        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("\"getBooks\""));
    }
    
}
