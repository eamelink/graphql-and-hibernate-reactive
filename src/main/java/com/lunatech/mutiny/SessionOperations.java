package com.lunatech.mutiny;

import io.smallrye.mutiny.Uni;
import org.hibernate.reactive.mutiny.Mutiny;

import java.util.function.Function;
import java.util.function.Supplier;

public class SessionOperations {

    public static <T> Uni<T> withTransactionSafe(Supplier<Uni<T>> work) {
        return withTransactionSafe(__ -> work.get());
    }

    public static <T> Uni<T> withTransactionSafe(Function<Mutiny.Transaction, Uni<T>> work) {
        return VertxContextExtras.acquireWithContextMutex(
                io.quarkus.hibernate.reactive.panache.common.runtime.SessionOperations::withTransaction,
                work);
    }

    public static <T> Uni<T> withSessionSafe(Function<Mutiny.Session, Uni<T>> work) {
        return VertxContextExtras.acquireWithContextMutex(
                io.quarkus.hibernate.reactive.panache.common.runtime.SessionOperations::withSession,
                work);
    }
}
