package main.java.by.naumenka.task1;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        sumValue();
        sumValueCountDown();
    }

    private static final Map<Integer, Integer> map = new HashMap<>();
    private static final ConcurrentHashMap<Integer, Integer> mapConcurrent = new ConcurrentHashMap<>();
    private static final Map<Integer, Integer> mapCustom = new ThreadSafeMap<>();
    private static final Integer COUNT_OF_NUMBERS = 1_000;

    private static void sumValue() throws InterruptedException {
        Runnable runnable = () -> {
            for (int i = 0; i < COUNT_OF_NUMBERS; i++) {
                map.put(i, i);
            }
        };
        Thread thread = new Thread(runnable);

        Runnable runnable1 = () -> {
            int sum = 0;

            for (int i = 0; i < COUNT_OF_NUMBERS; i++) {
                for (Integer key : map.keySet()) {
                    sum += map.get(key);
                }
            }

            System.out.println(sum);
        };
        Thread thread1 = new Thread(runnable1);

        thread.start();
        thread1.start();

        thread.join();
        thread1.join();
    }

    private static void sumValueCountDown() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);

        Thread thread = new Thread(() -> {
            for (int i = 0; i < COUNT_OF_NUMBERS; i++) {
                map.put(i, i);
            }
            countDownLatch.countDown();
        });

        Thread thread1 = new Thread(() -> {
            try {
                countDownLatch.await();
                int sum = 0;
                for (int i = 0; i < COUNT_OF_NUMBERS; i++) {
                    for (Integer key : map.keySet()) {
                        sum += map.get(key);
                    }
                }
                System.out.println(sum);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        thread.start();
        thread1.start();

        thread.join();
        thread1.join();
    }
}