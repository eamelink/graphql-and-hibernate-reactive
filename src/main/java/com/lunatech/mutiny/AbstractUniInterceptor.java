package com.lunatech.mutiny;

import io.smallrye.mutiny.Uni;
import jakarta.interceptor.InvocationContext;

// Copy of io.quarkus.hibernate.reactive.panache.common.runtime.AbstractUniInterceptor, which is not public
abstract class AbstractUniInterceptor {

    @SuppressWarnings("unchecked")
    protected <T> Uni<T> proceedUni(InvocationContext context) {
        try {
            return ((Uni<T>) context.proceed());
        } catch (Exception e) {
            return Uni.createFrom().failure(e);
        }
    }

    protected boolean isUniReturnType(InvocationContext context) {
        return context.getMethod().getReturnType().equals(Uni.class);
    }

}