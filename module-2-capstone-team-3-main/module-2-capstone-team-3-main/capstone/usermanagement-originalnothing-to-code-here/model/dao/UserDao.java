package com.techelevator.tenmo.usermanagement.model.dao;

import com.techelevator.tenmo.usermanagement.model.User;

import java.util.List;

public interface UserDao {

    List<User> findAll();

    User findUserById(int id);

    User findByUsername(String username);

    int findIdByUsername(String username);

    boolean create(String username, String password);
}
