package main.java.by.naumenka.task5;

import main.java.by.naumenka.task5.dao.UserDao;
import main.java.by.naumenka.task5.dao.UserDaoImpl;
import main.java.by.naumenka.task5.model.Currency;
import main.java.by.naumenka.task5.model.UserAccount;
import main.java.by.naumenka.task5.service.ExchangeService;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExchangePoint {
    public static void main(String[] args) throws InterruptedException {
        ExchangePoint exchanger = new ExchangePoint();
        exchanger.doExchange();
    }

    private final UserDao userDao;
    private final ExchangeService exchangeService;

    public ExchangePoint() {
        this.userDao = new UserDaoImpl();
        this.exchangeService = new ExchangeService();
    }

    public synchronized void doExchange() throws InterruptedException {
        Map<Currency, BigDecimal> currencyMap = new EnumMap<>(Currency.class);
        currencyMap.put(Currency.GEL, BigDecimal.valueOf(500.0));
        currencyMap.put(Currency.USD, BigDecimal.valueOf(1200.0));
        currencyMap.put(Currency.EUR, BigDecimal.valueOf(300.0));
        userDao.saveUser(new UserAccount("Client1", currencyMap));

        currencyMap.put(Currency.GEL, BigDecimal.valueOf(655.0));
        currencyMap.put(Currency.USD, BigDecimal.valueOf(70.0));
        currencyMap.put(Currency.EUR, BigDecimal.valueOf(80.0));
        userDao.saveUser(new UserAccount("Client2", currencyMap));

        Runnable runnable1 = () -> exchangeService.exchangeCurrency(
                "Client1", BigDecimal.valueOf(60), Currency.GEL, Currency.EUR);

        Runnable runnable2 = () -> exchangeService.exchangeCurrency(
                "Client2", BigDecimal.valueOf(30), Currency.GEL, Currency.USD);

        ExecutorService executorService = Executors.newFixedThreadPool(3);

        executorService.submit(runnable1);
        executorService.submit(runnable2);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        UserAccount user1 = userDao.getUser("Client1");
        System.out.println(user1.getCurrencyMap());

        UserAccount user2 = userDao.getUser("Client2");
        System.out.println(user2.getCurrencyMap());
    }
}
