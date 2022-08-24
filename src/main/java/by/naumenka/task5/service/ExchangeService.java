package main.java.by.naumenka.task5.service;

import main.java.by.naumenka.task5.dao.UserDaoImpl;
import main.java.by.naumenka.task5.model.Currency;
import main.java.by.naumenka.task5.model.UserAccount;
import main.java.by.naumenka.task5.utilities.NotEnoughMoneyException;
import main.java.by.naumenka.task5.utilities.UserNotFoundException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Logger;

public class ExchangeService {

    private static final Logger LOG = Logger.getLogger(ExchangeService.class.getName());
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final UserDaoImpl userDao = new UserDaoImpl();

    public void saveUser(UserAccount userAccount) {
        userDao.saveUser(userAccount);
    }

    public Optional<UserAccount> getUserAccount(String login) {
        lock.readLock().lock();
        Optional<UserAccount> user = Optional.ofNullable(userDao.getUser(login));
        lock.readLock().unlock();

        return user;
    }

    public void exchangeCurrency(String login, BigDecimal amount, Currency from, Currency to) {
        lock.writeLock().lock();
        LOG.info("Start exchange operation for client " + login);
        Optional<UserAccount> userAccount = getUserAccount(login);

        if (!userAccount.isPresent()) {
            throw new UserNotFoundException("Client with login " + login + " is not present");
        }

        if (checkIfEnoughMoneyForExchange(userAccount.get().getCurrencyMap(), amount, from)) {
            throw new NotEnoughMoneyException("Not enough money for exchange " + from.name() +
                    ", amount = " + amount + " for client " + userAccount.get().getLogin());
        }

        exchangeOperation(userAccount.get(), from, to, amount);

        lock.writeLock().unlock();
    }

    private void exchangeOperation(UserAccount userAccount, Currency from, Currency to, BigDecimal amount) {
        BigDecimal result = amount
                .multiply(BigDecimal.valueOf(from.getRate()))
                .divide(BigDecimal.valueOf(to.getRate()), 2, RoundingMode.HALF_UP);
        userAccount.getCurrencyMap().put(to, result);

        BigDecimal newValueAfterExchange = new BigDecimal(
                String.valueOf(userAccount.getCurrencyMap().get(from).subtract(amount)));
        userAccount.getCurrencyMap().put(from, newValueAfterExchange);

        saveUser(userAccount);
        LOG.info("Client: " + userAccount.getLogin() + " exchange " + from.name() + " to " + to.name() +
                " value amount: " + amount);
    }

    private boolean checkIfEnoughMoneyForExchange(Map<Currency, BigDecimal> currencyMap, BigDecimal amount, Currency from) {
        return currencyMap.get(from).compareTo(amount) < 0;
    }
}