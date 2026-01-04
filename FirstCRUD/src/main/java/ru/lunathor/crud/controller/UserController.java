package ru.lunathor.crud.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.lunathor.crud.dto.UserFormDto;
import ru.lunathor.crud.model.User;
import ru.lunathor.crud.service.RoleService;
import ru.lunathor.crud.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class UserController {

    private final UserService userService;
    private final RoleService roleService;

    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

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
        model.addAttribute("userForm", new UserFormDto());
        model.addAttribute("allRoles", roleService.getAllRoles());
        return "users/form";
    }

    @PostMapping("/admin/save")
    public String saveUser(@ModelAttribute UserFormDto userForm) {
        userService.saveUser(userForm);
        return "redirect:/admin/list";
    }

    @GetMapping("/admin/edit")
    public String showEditForm(@RequestParam("id") Long id, Model model) {
        User user = userService.getUserById(id);
        UserFormDto userForm = new UserFormDto();
        userForm.setId(user.getId());
        userForm.setUsername(user.getUsername());
        userForm.setName(user.getName());
        userForm.setSurname(user.getSurname());
        userForm.setEmail(user.getEmail());
        userForm.setAge(user.getAge());
        if (user.getRoles() != null) {
            userForm.setRoleIds(user.getRoles().stream()
                    .map(role -> role.getId())
                    .collect(Collectors.toList()));
        }
        model.addAttribute("userForm", userForm);
        model.addAttribute("allRoles", roleService.getAllRoles());
        return "users/form";
    }

    @PostMapping("/admin/update")
    public String updateUser(@ModelAttribute UserFormDto userForm) {
        userService.updateUser(userForm);
        return "redirect:/admin/list";
    }

    @PostMapping("/admin/delete")
    public String deleteUser(@RequestParam("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin/list";
    }

    @GetMapping("/user")
    public String showUserPage(Authentication authentication, Model model) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.getUserByUsername(userDetails.getUsername());
        model.addAttribute("user", user);
        return "users/user";
    }
}
