package ru.lunathor.crud.service;

import ru.lunathor.crud.dto.UserFormDto;
import ru.lunathor.crud.model.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();

    void saveUser(UserFormDto userFormDto);

    User getUserById(Long id);

    User getUserByUsername(String username);

    void updateUser(UserFormDto userFormDto);

    void updateUserDirect(User user);

    void deleteUser(Long id);
}
