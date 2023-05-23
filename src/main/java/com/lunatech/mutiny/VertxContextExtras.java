package com.lunatech.mutiny;

import io.smallrye.mutiny.Uni;
import io.vertx.core.Context;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.function.Supplier;

public class VertxContextExtras {
    private static final Logger logger = LoggerFactory.getLogger(VertxContextExtras.class);
    private static final String MUTEX_KEY = "hibernate-mutex";


    public static <T> Uni<T> contextMutex(Supplier<Uni<T>> inner) {
        return Uni.createFrom().deferred(() -> {
            Context ctx = Vertx.currentContext();
            UniSemaphore hibernateMutex = ctx.getLocal(MUTEX_KEY);
            if(hibernateMutex == null) {
                if(logger.isTraceEnabled()) {
                    logger.trace(Thread.currentThread().getName() + ": Creating hibernate mutex");
                }
                hibernateMutex = MutinyConcurrency.mutex();
                ctx.putLocal(MUTEX_KEY, hibernateMutex);
            } else {
                if(logger.isTraceEnabled()) {
                    logger.trace(Thread.currentThread().getName() + ": Found existing hibernate mutex");
                }
            }
            return hibernateMutex.protect(inner);
        });
    }

}
