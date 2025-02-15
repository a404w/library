package de.thws.fiw.bs.library.infrastructure.server;

import graphql.kickstart.servlet.GraphQLHttpServlet;
import graphql.kickstart.servlet.GraphQLConfiguration;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import de.thws.fiw.bs.library.application.graphql.GraphQLHandler;


public class GraphQLServlet extends GraphQLHttpServlet {

    @Override
    protected GraphQLConfiguration getConfiguration() {
        return GraphQLConfiguration.with(GraphQLHandler.getGraphQLSchema()).build();
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        StringBuilder requestBody = new StringBuilder();
    
        try (BufferedReader reader = req.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }
        } catch (IOException e) {
            System.err.println("❌ Fehler beim Lesen der Anfrage: " + e.getMessage());
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }
    
        // 🔴 PRINT THE RAW INCOMING REQUEST
        System.out.println("📥 Eingehende Anfrage: " + requestBody.toString());
    
        String jsonResponse = GraphQLHandler.handleRequest(requestBody.toString());
    
        resp.setContentType("application/json");
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        resp.setStatus(HttpServletResponse.SC_OK);
    
        try (PrintWriter writer = resp.getWriter()) {
            writer.write(jsonResponse);
    
            // 🔴 PRINT THE RESPONSE SENT BACK TO THE CLIENT
            System.out.println("📤 Antwort an Client: " + jsonResponse);
        } catch (IOException e) {
            System.err.println("❌ Fehler beim Senden der Antwort: " + e.getMessage());
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}    