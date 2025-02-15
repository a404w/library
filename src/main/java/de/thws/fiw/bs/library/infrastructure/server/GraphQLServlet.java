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
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }
    
        String jsonResponse = GraphQLHandler.handleRequest(requestBody.toString());
    
        resp.setContentType("application/json");
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        resp.setStatus(HttpServletResponse.SC_OK);
    
        try (PrintWriter writer = resp.getWriter()) {
            writer.write(jsonResponse);
        } catch (IOException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
    
}
