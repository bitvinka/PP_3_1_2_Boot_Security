package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;
import ru.kata.spring.boot_security.demo.util.UserValidator;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/admin/user")
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;
    private final UserValidator userValidator;
    private final PasswordEncoder passwordEncoder;
    private static final String REDIRECT_ADMIN = "redirect:/admin";

    @Autowired
    public AdminController(UserService userService, RoleService roleService, UserValidator userValidator, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.userValidator = userValidator;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("")
    public String allUsers(Model model) {
        model.addAttribute("users", userService.getUsers());
        return "admin/adminMain";
    }

    @GetMapping("/showUser")
    public String showUser(@RequestParam(value = "id") Long id, Model model){
       model.addAttribute("user", userService.getUserById(id).get());
        return "admin/userAdmin";
    }

    @GetMapping("/new")
    public String addUser(Model model) {
        model.addAttribute("user", new User());
        return "admin/newUserForm";
    }

    @PostMapping("/new")
    public String addUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult) {
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            return "admin/newUserForm";
        }
        userService.addUser(user);
        return REDIRECT_ADMIN;
    }


    @GetMapping("/edit")
    public String editUser(@RequestParam(value = "id") Long id, Model model) {
        Optional<User> optUser = userService.getUserById(id);
        optUser.ifPresent(user -> model.addAttribute("editUser", user));
        model.addAttribute("roles", roleService.getRoles());
        return "admin/editUserForm";
    }

    @PostMapping("/edit")
    public String editUserForm(@ModelAttribute("editUser") @Valid User user, BindingResult bindingResult, @RequestParam(value = "id") Long id) {
        Optional<User> optUser = userService.getUserById(user.getId());
        if (optUser.isPresent() && (!user.getEmail().equals(optUser.get().getEmail()))) {
            userValidator.validate(user, bindingResult);
        }
        if (bindingResult.hasErrors()) {
            return "admin/editUserForm";
        }
        if (optUser.isPresent() && (!user.getPassword().equals(optUser.get().getPassword()))) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        System.out.println("-------");
        for (Role r: user.getRoles()){
            System.out.println(r.getName());
            System.out.println(r.getAuthority());
            System.out.println(r.getId());
            System.out.println("ЮЗЕР-РОЛЬ");
        }
        userService.updateUser(user);
        return REDIRECT_ADMIN;
    }

    @GetMapping("/delete")
    public String deleteUser(@RequestParam(value = "id") Long id) {
        userService.removeUser(id);
        return REDIRECT_ADMIN;
    }
}
