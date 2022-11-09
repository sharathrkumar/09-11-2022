package com.virtuallearn.Authentication.virtualLearn.register.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistration {

    private String mobileNumber;
    private String fullName;
    private String userName;
    private String email;
    private String password;

}
