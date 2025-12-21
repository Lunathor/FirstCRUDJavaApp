package ru.lunathor.crud.dao;

import ru.lunathor.crud.model.User;
import java.util.List;

public interface UserDao {
    List<User> getAllUsers();
    void saveUser(User user);
    User getUserById(Long id);
    void updateUser(User user);
    void deleteUser(Long id);
}
