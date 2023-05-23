package org.acme;

import com.lunatech.mutiny.ContextMutex;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
@ContextMutex
public class PeopleRepository implements PanacheRepository<Person> {
}
