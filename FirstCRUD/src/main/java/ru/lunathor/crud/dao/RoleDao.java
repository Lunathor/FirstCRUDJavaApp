package ru.lunathor.crud.dao;

import ru.lunathor.crud.model.Role;
import java.util.List;

public interface RoleDao {
    List<Role> getAllRoles();
    Role getRoleById(Long id);
    Role getRoleByName(String name);
    void saveRole(Role role);
}

