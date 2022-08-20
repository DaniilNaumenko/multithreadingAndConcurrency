package main.java.by.naumenka.task4;

public class Task4 {
    public static void main(String[] args) throws InterruptedException {
        BlockingObjectPool pool = new BlockingObjectPool(2);

        System.out.println("get " + pool.get());
        System.out.println("get " + pool.get());

        Object obj = new Object();
        pool.take(obj);
        System.out.println("take " + obj);

        System.out.println("get " + pool.get());
        System.out.println("get " + pool.get());
        System.out.println("get " + pool.get());
    }
}
