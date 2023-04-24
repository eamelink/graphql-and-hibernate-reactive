package org.acme;

import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
@WithTransaction
public class PeopleService {

    public Uni<List<Person>> people() {
        return Person.listAll();
    }

    public Uni<List<Foo>> foos(Person person) {
        return Foo.list("personId", person.id);
    }
}
