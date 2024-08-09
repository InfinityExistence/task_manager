package ru.effectivemobile.taskmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.effectivemobile.taskmanager.entity.dao.User;
import ru.effectivemobile.taskmanager.service.UserDataService;

import java.util.UUID;

@RestController
@RequestMapping("/api/user/")
public class UserController {
    @Autowired
    UserDataService userDataService;

    @GetMapping("all")
    Page<User> viewAllUsers(@RequestParam(defaultValue = "0", required = false) int page) {
        return userDataService.getPageOfAllUsers(page);
    }

    @GetMapping()
    User viewUserById(@RequestParam(required = false) UUID id, Authentication authentication) {
        if (id == null)
            return userDataService.getUserByEmail(authentication.getName());
        return userDataService.getUserByUuid(id);
    }
}
