package org.acme;

import com.lunatech.mutiny.ContextMutex;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
@WithSession
@ContextMutex
public class PeopleService {

    public Uni<List<Person>> people() {
        return Person.listAll();
    }

    public Uni<List<Foo>> foos(Person person) {
        return Foo.<Foo>list("personId", person.id)
                .invoke(foos -> System.out.println("Got " + foos));
    }
}
