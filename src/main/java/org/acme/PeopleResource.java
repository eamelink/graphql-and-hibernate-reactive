package org.acme;

import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Query;
import org.eclipse.microprofile.graphql.Source;

import java.util.List;

@GraphQLApi
public class PeopleResource {

    private final PeopleService peopleService;

    public PeopleResource(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @Query
    public Uni<List<Person>> users() {
        return peopleService.people();
    }

    public Uni<List<Foo>> foos(@Source Person person) {
        return peopleService.foos(person);
    }
}