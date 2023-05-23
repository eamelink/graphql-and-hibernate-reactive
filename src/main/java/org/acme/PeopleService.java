package org.acme;

import com.lunatech.mutiny.ContextMutex;
import com.lunatech.mutiny.WithSessionSafe;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
@WithSessionSafe
public class PeopleService {

    private final PeopleRepository peopleRepository;
    private final FooRepository fooRepository;

    public PeopleService(PeopleRepository peopleRepository, FooRepository fooRepository) {
        this.peopleRepository = peopleRepository;
        this.fooRepository = fooRepository;
    }

    public Uni<List<Person>> people() {
        return peopleRepository.listAll();
    }

    public Uni<List<Foo>> foos(Person person) {
        return fooRepository.list("personId", person.id)
                .invoke(foos -> System.out.println("Got " + foos));
    }
}
