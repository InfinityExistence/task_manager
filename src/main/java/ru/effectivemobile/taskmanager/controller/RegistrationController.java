package ru.effectivemobile.taskmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.effectivemobile.taskmanager.entity.dao.PasswordUserDetails;
import ru.effectivemobile.taskmanager.entity.request.SignUpRequest;
import ru.effectivemobile.taskmanager.service.RegistrationService;

@RestController
public class RegistrationController {
    @Autowired
    RegistrationService registrationService;

    @PostMapping("/api/auth/register")
    ResponseEntity<?> postRegister(@RequestBody SignUpRequest signUpRequest) {

        PasswordUserDetails userDetails = PasswordUserDetails.builder()
                .email(signUpRequest.email())
                .password(signUpRequest.password())
                .build();

        registrationService.register(userDetails, signUpRequest.name());

        return ResponseEntity.ok("User registration success");
    }


}
