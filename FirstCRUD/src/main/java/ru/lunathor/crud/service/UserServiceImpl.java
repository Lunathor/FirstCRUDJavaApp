package ru.lunathor.crud.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.lunathor.crud.dao.UserDao;
import ru.lunathor.crud.dto.UserFormDto;
import ru.lunathor.crud.model.Role;
import ru.lunathor.crud.model.User;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserDao userDao;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserDao userDao, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    @Override
    public void saveUser(UserFormDto userFormDto) {
        User user = new User();
        user.setUsername(userFormDto.getUsername());
        user.setPassword(passwordEncoder.encode(userFormDto.getPassword()));
        user.setName(userFormDto.getName());
        user.setSurname(userFormDto.getSurname());
        user.setEmail(userFormDto.getEmail());
        user.setAge(userFormDto.getAge());

        Set<Role> roles = new HashSet<>();
        if (userFormDto.getRoleIds() != null && !userFormDto.getRoleIds().isEmpty()) {
            roles = userFormDto.getRoleIds().stream()
                    .map(roleService::getRoleById)
                    .collect(Collectors.toSet());
        }
        user.setRoles(roles);

        userDao.saveUser(user);
    }

    @Override
    public User getUserById(Long id) {
        return userDao.getUserById(id);
    }

    @Override
    public User getUserByUsername(String username) {
        return userDao.getUserByUsername(username);
    }

    @Override
    public void updateUser(UserFormDto userFormDto) {
        User existingUser = userDao.getUserById(userFormDto.getId());
        if (existingUser != null) {
            existingUser.setUsername(userFormDto.getUsername());
            existingUser.setName(userFormDto.getName());
            existingUser.setSurname(userFormDto.getSurname());
            existingUser.setEmail(userFormDto.getEmail());
            existingUser.setAge(userFormDto.getAge());

            if (userFormDto.getPassword() != null && !userFormDto.getPassword().isEmpty()) {
                existingUser.setPassword(passwordEncoder.encode(userFormDto.getPassword()));
            }

            Set<Role> roles = new HashSet<>();
            if (userFormDto.getRoleIds() != null && !userFormDto.getRoleIds().isEmpty()) {
                roles = userFormDto.getRoleIds().stream()
                        .map(roleService::getRoleById)
                        .collect(Collectors.toSet());
            }
            existingUser.setRoles(roles);

            userDao.updateUser(existingUser);
        }
    }

    @Override
    public void updateUserDirect(User user) {
        userDao.updateUser(user);
    }

    @Override
    public void deleteUser(Long id) {
        userDao.deleteUser(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.getUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }
        return user;
    }
}
