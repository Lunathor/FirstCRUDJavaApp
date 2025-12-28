package ru.lunathor.crud.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ru.lunathor.crud.model.Role;
import ru.lunathor.crud.model.User;
import ru.lunathor.crud.service.RoleService;
import ru.lunathor.crud.service.UserService;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class UserController {

    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    // Admin endpoints - CRUD operations
    @RequestMapping("/admin/")
    public String adminIndex() {
        return "redirect:/admin/list";
    }

    @GetMapping("/admin/list")
    public String getAllUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "users/list";
    }

    @GetMapping("/admin/new")
    public String showAddForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("allRoles", roleService.getAllRoles());
        return "users/form";
    }

    @PostMapping("/admin/save")
    public String saveUser(@RequestParam(value = "roleIds", required = false) List<Long> roleIds,
                          User user) {
        if (roleIds != null && !roleIds.isEmpty()) {
            Set<Role> roles = roleIds.stream()
                    .map(roleService::getRoleById)
                    .collect(Collectors.toSet());
            user.setRoles(roles);
        }
        // Encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.saveUser(user);
        return "redirect:/admin/list";
    }

    @GetMapping("/admin/edit")
    public String showEditForm(@RequestParam("id") Long id,
                               Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        model.addAttribute("allRoles", roleService.getAllRoles());
        return "users/form";
    }

    @PostMapping("/admin/update")
    public String updateUser(@RequestParam(value = "roleIds", required = false) List<Long> roleIds,
                            User user) {
        User existingUser = userService.getUserById(user.getId());
        if (existingUser != null) {
            // Update roles
            if (roleIds != null && !roleIds.isEmpty()) {
                Set<Role> roles = roleIds.stream()
                        .map(roleService::getRoleById)
                        .collect(Collectors.toSet());
                user.setRoles(roles);
            } else {
                user.setRoles(new HashSet<>());
            }
            // If password is provided and not empty, encode it; otherwise keep existing password
            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            } else {
                user.setPassword(existingUser.getPassword());
            }
            userService.updateUser(user);
        }
        return "redirect:/admin/list";
    }

    @PostMapping("/admin/delete")
    public String deleteUser(@RequestParam("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin/list";
    }

    // User endpoint - show current user's data
    @GetMapping("/user")
    public String showUserPage(Authentication authentication, Model model) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.getUserByUsername(userDetails.getUsername());
        model.addAttribute("user", user);
        return "users/user";
    }
}
