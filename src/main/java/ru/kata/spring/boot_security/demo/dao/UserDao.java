package ru.kata.spring.boot_security.demo.dao;

import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {

    List<User> getUsers();
    Optional<User> getUserById(Long id);

    void removeUser(Long id);
    void updateUser(User user, Long id);
    Optional<User> findByEmail(String email);
    boolean addUser(User user);

}
