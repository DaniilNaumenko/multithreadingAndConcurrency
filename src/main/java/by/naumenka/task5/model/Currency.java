package main.java.by.naumenka.task5.model;

public enum Currency {

    GEL(1.0),
    USD(0.36),
    EUR(0.35);

    private final double rate;

    Currency(double rate) {
        this.rate = rate;
    }

    public double getRate() {
        return rate;
    }
}