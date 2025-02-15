package de.thws.fiw.bs.library.infrastructure.server;

import graphql.kickstart.servlet.GraphQLHttpServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class GraphQLServer {
    public static void main(String[] args) {
        try {
            Server server = new Server(8080);
            ServletContextHandler context = new ServletContextHandler();
            context.setContextPath("/");

            // Korrekte Registrierung des GraphQL-Servlets
            ServletHolder servletHolder = new ServletHolder(GraphQLHttpServlet.class);
            servletHolder.setInitParameter("graphql.schema.factory",
                    "de.thws.fiw.bs.library.application.graphql.GraphQLHandler");

            context.addServlet(servletHolder, "/graphql");

            server.setHandler(context);

            System.out.println("✅ GraphQL Server läuft auf Port 8080...");
            server.start();
            server.join();
        } catch (Exception e) {
            System.err.println("❌ Fehler beim Starten des GraphQL-Servers:");
            e.printStackTrace();
        }
    }
}
