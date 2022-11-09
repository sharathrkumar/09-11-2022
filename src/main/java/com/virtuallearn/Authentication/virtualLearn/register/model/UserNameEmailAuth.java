package com.virtuallearn.Authentication.virtualLearn.register.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserNameEmailAuth {

    private String userName;
    private String email;

}
