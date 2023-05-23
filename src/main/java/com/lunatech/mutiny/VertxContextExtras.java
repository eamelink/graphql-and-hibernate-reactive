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

    public static <R, T> Uni<T> acquireWithContextMutex(
            Function<Function<R, Uni<T>>, Uni<T>> acquirer,
            Function<R, Uni<T>> work) {

        return Uni.createFrom().deferred(() -> {
            Context ctx = Vertx.currentContext();
            UniSemaphoreImpl hibernateMutex = ctx.getLocal(MUTEX_KEY);
            if(hibernateMutex == null) {
                if(logger.isTraceEnabled()) {
                    logger.trace("Creating hibernate mutex in 'acquireWithContextMutex'");
                }
                hibernateMutex = new UniSemaphoreImpl(1);
                ctx.putLocal(MUTEX_KEY, hibernateMutex);
            } else {
                if(logger.isTraceEnabled()) {
                    logger.trace("Found existing hibernate mutex in 'acquireWithContextMutex'");
                }
            }
            final UniSemaphoreImpl hibernateMutexF = hibernateMutex;

            AtomicBoolean released = new AtomicBoolean(false);

            return hibernateMutex.acquire().chain(() ->
                    acquirer.apply(resource -> {
                        if(logger.isTraceEnabled()) {
                            logger.trace("Resource acquired, releasing permit");
                        }
                        released.set(true);
                        return hibernateMutexF.release().chain(() -> work.apply(resource));
                    }).eventually(() -> {
                        if(!released.get()) {
                            if(logger.isDebugEnabled()) {
                                // Logging at debug level instead of trace; this means that resource
                                // acquisition didn't complete succesfully, which is odd.
                                logger.debug("Permit not released yet in eventually, releasing now");
                            }
                            return hibernateMutexF.release();
                        } else {
                            return Uni.createFrom().voidItem();
                        }
                    }));
        });
    }
}
