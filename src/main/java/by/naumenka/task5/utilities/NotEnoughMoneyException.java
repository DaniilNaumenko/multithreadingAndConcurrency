package main.java.by.naumenka.task5.utilities;

public class NotEnoughMoneyException extends RuntimeException{
    public NotEnoughMoneyException() {
        super();
    }

    public NotEnoughMoneyException(String message) {
        super(message);
    }
}
