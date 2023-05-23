package org.acme;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.Vertx;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Query;
import org.eclipse.microprofile.graphql.Source;

import java.util.List;

@GraphQLApi
public class PeopleResource {

    private final PeopleService peopleService;
    private final Vertx vertx;

    public PeopleResource(PeopleService peopleService, Vertx vertx) {
        this.peopleService = peopleService;
        this.vertx = vertx;
    }

    @Query
    public Uni<List<Person>> users() {
        return peopleService.people();
    }

    public Uni<List<Foo>> foos(@Source Person person) {
        return peopleService.foos(person);
    }

}