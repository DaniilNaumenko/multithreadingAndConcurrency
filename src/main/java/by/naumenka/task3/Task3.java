package main.java.by.naumenka.task3;

import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ThreadLocalRandom;

public class Task3 {

    private final static Integer COUNT_OF_MASSAGE = 5;

    public static void main(String[] args) throws InterruptedException {
        final ProducerConsumer producerConsumer = new ProducerConsumer();

        Thread producer = new Thread(() -> {
            try {
                for (int i = 0; i < COUNT_OF_MASSAGE; i++) {
                    producerConsumer.produce();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread consumer = new Thread(() -> {
            try {
                for (int i = 0; i < COUNT_OF_MASSAGE; i++){
                    producerConsumer.consume();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        producer.start();
        consumer.start();

        consumer.join();
        producer.join();
    }

    public static class ProducerConsumer {
        private final Queue<Integer> massageBus = new PriorityQueue<>();
        private final static Integer CAPACITY = 10;

        public void produce() throws InterruptedException {

            synchronized (this) {
                while (massageBus.size() == CAPACITY) {
                    try {
                        wait();
                    } catch (InterruptedException exception) {
                        Thread.currentThread().interrupt();
                    }
                }

                Integer value = ThreadLocalRandom.current().nextInt(10);
                System.out.println("produce + " + value);
                massageBus.offer(value);

                System.out.println("after produce " + massageBus);
                notifyAll();

                Thread.sleep(100);
            }
        }

        public void consume() throws InterruptedException {
            synchronized (this) {
                while (massageBus.isEmpty()) {
                    wait();
                }

                System.out.println("consume - " + massageBus.poll());
                System.out.println("after consume " + massageBus);
                notifyAll();

                Thread.sleep(100);
            }
        }
    }
}