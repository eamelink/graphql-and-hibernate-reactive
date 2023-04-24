package org.acme;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;

import jakarta.persistence.Entity;

@Entity
public class Foo extends PanacheEntity {

    public Long personId;
    public String foo;
}
