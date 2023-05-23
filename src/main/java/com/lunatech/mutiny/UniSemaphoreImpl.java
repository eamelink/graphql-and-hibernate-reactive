package com.lunatech.mutiny;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.subscription.UniEmitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.function.Supplier;

public class UniSemaphoreImpl implements UniSemaphore {
    private static final Logger logger = LoggerFactory.getLogger(UniSemaphoreImpl.class);
    private int permits;
    private final Queue<UniEmitter<Void>> queue;

    public UniSemaphoreImpl(int permits) {
        assert(permits > 0);
        this.permits = permits;
        queue = new LinkedBlockingDeque<>();
    }

    @Override
    public <T> Uni<T> protect(Supplier<Uni<T>> uni) {
        return acquire().chain(uni::get).eventually(this::release);
    }

    Uni<Void> release() {

        return Uni.createFrom().item(() -> {
            synchronized (this) {
                UniEmitter<Void> next = queue.poll();
                if (next == null) {
                    if(logger.isTraceEnabled()) {
                        logger.trace("Releasing, nothing queued, returning permit");
                    }
                    permits++;
                } else {
                    if(logger.isTraceEnabled()) {
                        logger.trace("Releasing, something queued, triggering");
                    }
                    next.complete(null);
                }
                return null;
            }
        });
    }

    Uni<Void> acquire() {
        return Uni.createFrom().deferred(() -> {

            synchronized (this) {
                if (permits >= 1) {
                    if(logger.isTraceEnabled()) {
                        logger.trace("Acquiring, got a permit");
                    }
                    permits--;
                    return Uni.createFrom().voidItem();
                } else {
                    if(logger.isTraceEnabled()) {
                        logger.trace("Acquiring, no a permit, queuing");
                    }
                    return Uni.createFrom().emitter(emitter -> queue.add((UniEmitter<Void>) emitter));
                }
            }
        });
    }
}
