package de.thws.fiw.bs.library.application.graphql;
import de.thws.fiw.bs.library.application.graphql.GraphQLHandler;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GraphQLHandlerTest {

    @Test
    void testQueryBooks() {
        String query = "{ \"query\": \"{ books { id title } }\" }";
        String response = GraphQLHandler.handleRequest(query);

        // 🔥 Debugging: Die tatsächliche Antwort ausgeben
        System.out.println("📥 Gesendete Anfrage: " + query);
        System.out.println("📤 Antwort von GraphQL: " + response);

        assertNotNull(response, "❌ Die Antwort darf nicht null sein!");
        assertTrue(response.contains("books"), "❌ Die Antwort enthält keine Bücher!");
    }

    @Test
    void testMutationAddBook() {
        String mutation = "{ \"query\": \"mutation { addBook(book: { title: \\\"GraphQL for Beginners\\\", isbn: \\\"978-1234567890\\\", isAvailable: true }) { id title } }\" }";
        String response = GraphQLHandler.handleRequest(mutation);

        // 🔥 Debugging: Die tatsächliche Antwort ausgeben
        System.out.println("📥 Gesendete Mutation: " + mutation);
        System.out.println("📤 Antwort von GraphQL: " + response);

        assertNotNull(response, "❌ Die Antwort darf nicht null sein!");
        assertTrue(response.contains("GraphQL for Beginners"), "❌ Das Buch wurde nicht hinzugefügt!");
    }
}
