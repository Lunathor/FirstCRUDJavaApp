package ru.lunathor.crud.service;

import ru.lunathor.crud.model.User;
import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    void saveUser(User user);
    User getUserById(Long id);
    User getUserByUsername(String username);
    void updateUser(User user);
    void deleteUser(Long id);
}
