package main.java.by.naumenka.task4;

import java.util.ArrayList;
import java.util.List;
import static java.util.stream.IntStream.range;

public class BlockingObjectPool {

    private final List<Object> pool = new ArrayList<>();
    private static final Integer POOL_SIZE = 4;

    public BlockingObjectPool(int size) {
        synchronized (pool) {
            range(0, size)
                    .forEach(i -> pool.add(new Object()));
        }
    }

    public synchronized Object get() throws InterruptedException {
        while (pool.isEmpty()) {
            this.wait();
        }
        Object object = pool.remove(0);
        notifyAll();
        return object;

    }

    public synchronized void take(Object object) throws InterruptedException {
        while (pool.size() >= POOL_SIZE) {
            wait();
        }
        pool.add(object);
        notifyAll();
    }
}