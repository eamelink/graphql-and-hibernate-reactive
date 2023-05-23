package com.lunatech.mutiny;

import io.smallrye.mutiny.Uni;

import java.util.function.Supplier;

public interface UniSemaphore {
    <T> Uni<T> protect(Supplier<Uni<T>> inner);
}
