package main.java.by.naumenka.task5.dao;

import main.java.by.naumenka.task5.model.Currency;
import main.java.by.naumenka.task5.model.UserAccount;
import main.java.by.naumenka.task5.utilities.UserNotFoundException;

import java.io.*;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Logger;

public class UserDaoImpl implements UserDao {

    private static final Logger LOG = Logger.getLogger(UserDaoImpl.class.getName());

    @Override
    public void saveUser(UserAccount userAccount) {
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(userAccount.getLogin());
        } catch (IOException e) {
            e.printStackTrace();
        }
        PrintWriter printWriter = new PrintWriter(fileWriter);
        userAccount.getCurrencyMap().forEach((key, value) -> printWriter.print(key + " " + value + " "));
        LOG.info("Save client " + userAccount.getLogin());
        printWriter.close();
    }

    @Override
    public UserAccount getUser(String login) {
        LOG.info("Getting client " + login);

        Map<Currency, BigDecimal> currencyMap = new HashMap<>();
        try {
            FileReader file = new FileReader(login);
            Scanner sc = new Scanner(file);
            sc.useDelimiter(" ");

            while (sc.hasNext()) {
                currencyMap.put(Currency.valueOf(sc.next()), new BigDecimal(sc.next()));
            }
        } catch (FileNotFoundException e) {
            LOG.warning("Error while reading client");
            throw new UserNotFoundException(e.getMessage());
        }

        return new UserAccount(login, currencyMap);
    }
}