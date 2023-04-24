package org.acme;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;

import jakarta.persistence.Entity;

@Entity
public class Person extends PanacheEntity {
    public String name;
}
