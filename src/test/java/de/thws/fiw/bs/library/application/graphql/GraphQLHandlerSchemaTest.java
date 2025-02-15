package de.thws.fiw.bs.library.application.graphql;

import graphql.schema.GraphQLSchema;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GraphQLHandlerSchemaTest {

    @Test
    void testSchemaLoading() {
        GraphQLSchema schema = GraphQLHandler.getGraphQLSchema();
        assertNotNull(schema, "❌ GraphQL-Schema konnte nicht geladen werden!");
    }
}
