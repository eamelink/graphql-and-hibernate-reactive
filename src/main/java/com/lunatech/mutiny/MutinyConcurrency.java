package com.lunatech.mutiny;

public class MutinyConcurrency {
    private MutinyConcurrency(){}

    public static UniSemaphore mutex() {
        return semaphore(1);
    }

    public static UniSemaphore semaphore(int permits) {
        return new UniSemaphoreImpl(permits);
    }
}
