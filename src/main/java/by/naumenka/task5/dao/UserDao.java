package main.java.by.naumenka.task5.dao;

import main.java.by.naumenka.task5.model.UserAccount;

public interface UserDao {

    void saveUser(UserAccount userAccount);

    UserAccount getUser(String login);
}