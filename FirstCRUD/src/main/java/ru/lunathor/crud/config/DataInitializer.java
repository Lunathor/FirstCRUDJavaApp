package ru.lunathor.crud.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.lunathor.crud.model.Role;
import ru.lunathor.crud.model.User;
import ru.lunathor.crud.service.RoleService;
import ru.lunathor.crud.service.UserService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleService roleService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(RoleService roleService, UserService userService, PasswordEncoder passwordEncoder) {
        this.roleService = roleService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // Initialize roles if they don't exist
        Role adminRole;
        if (roleService.getRoleByName("ADMIN") == null) {
            adminRole = new Role("ADMIN");
            roleService.saveRole(adminRole);
        } else {
            adminRole = roleService.getRoleByName("ADMIN");
        }
        
        if (roleService.getRoleByName("USER") == null) {
            Role userRole = new Role("USER");
            roleService.saveRole(userRole);
        }
        
        // Process all existing users
        List<User> allUsers = userService.getAllUsers();
        for (User user : allUsers) {
            boolean needsUpdate = false;
            
            // If user has no roles, assign ADMIN role
            if (user.getRoles() == null || user.getRoles().isEmpty()) {
                Set<Role> roles = new HashSet<>();
                roles.add(adminRole);
                user.setRoles(roles);
                needsUpdate = true;
                System.out.println("Assigned ADMIN role to user: " + (user.getUsername() != null ? user.getUsername() : "ID:" + user.getId()));
            }
            
            // If user has no username, set it based on email or id
            if (user.getUsername() == null || user.getUsername().isEmpty()) {
                if (user.getEmail() != null && !user.getEmail().isEmpty()) {
                    user.setUsername(user.getEmail());
                } else {
                    user.setUsername("user" + user.getId());
                }
                needsUpdate = true;
            }
            
            // Check and fix password encoding
            if (user.getPassword() == null || user.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode("admin"));
                needsUpdate = true;
                System.out.println("Set default password 'admin' for user: " + user.getUsername());
            } else if (!isBCryptHash(user.getPassword())) {
                // If password exists but is not BCrypt, re-encode it
                // Since we can't know the original password, we set it to "admin"
                user.setPassword(passwordEncoder.encode("admin"));
                needsUpdate = true;
                System.out.println("Re-encoded password to BCrypt for user: " + user.getUsername() + " (new password: admin)");
            }
            
            if (needsUpdate) {
                userService.updateUser(user);
            }
        }
    }
    
    /**
     * Checks if a string is a BCrypt hash.
     * BCrypt hashes start with $2a$, $2b$, or $2y$ followed by cost parameter and 53 characters.
     */
    private boolean isBCryptHash(String password) {
        if (password == null || password.length() < 60) {
            return false;
        }
        return password.startsWith("$2a$") || 
               password.startsWith("$2b$") || 
               password.startsWith("$2y$");
    }
}

