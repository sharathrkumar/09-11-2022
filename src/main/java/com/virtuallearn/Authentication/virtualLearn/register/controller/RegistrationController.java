package com.virtuallearn.Authentication.virtualLearn.register.controller;


import com.virtuallearn.Authentication.virtualLearn.register.model.UserRegistration;
import com.virtuallearn.Authentication.virtualLearn.register.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistrationController {

    @Autowired
    RegistrationService registrationService;



    @PostMapping("/Register")
    public ResponseEntity<String> registration(@RequestBody UserRegistration registration){
        String status = registrationService.addDetails(registration);
        if(status == null)
            return new ResponseEntity<String>("User Created", HttpStatus.CREATED);
        return new ResponseEntity<String>(status, HttpStatus.NOT_ACCEPTABLE);
    }

}
