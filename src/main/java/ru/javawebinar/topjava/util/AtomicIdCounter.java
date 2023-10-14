package ru.javawebinar.topjava.util;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicIdCounter {
    private static AtomicInteger counter = new AtomicInteger(100);

    public static Integer nextId() {
        return (counter.incrementAndGet());
    }
}