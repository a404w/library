package de.thws.fiw.bs.library.infrastructure.server;
import de.thws.fiw.bs.library.infrastructure.server.GraphQLServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;


public class GraphQLServer {
    public static void main(String[] args) {
        try {
            Server server = new Server(8080);
            ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
            context.setContextPath("/");
            
            // Servlet direkt über die Klasse registrieren
            ServletHolder servletHolder = new ServletHolder(new GraphQLServlet());
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