package main.java.by.naumenka.task5.utilities;

import java.io.FileNotFoundException;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException() {
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}