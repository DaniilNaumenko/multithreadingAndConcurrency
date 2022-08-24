package main.java.by.naumenka.task5.model;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.Map;

public class UserAccount {

    private String login;
    private Map<Currency, BigDecimal> currencyMap = new EnumMap<>(Currency.class);

    public UserAccount(String login, Map<Currency, BigDecimal> currencyMap) {
        this.login = login;
        this.currencyMap = currencyMap;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Map<Currency, BigDecimal> getCurrencyMap() {
        return currencyMap;
    }

    public void setCurrencyMap(Map<Currency, BigDecimal> currencyMap) {
        this.currencyMap = currencyMap;
    }
}