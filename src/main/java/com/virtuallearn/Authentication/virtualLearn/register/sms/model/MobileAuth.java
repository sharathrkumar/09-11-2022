package com.virtuallearn.Authentication.virtualLearn.register.sms.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MobileAuth {

    private String mobileNumber;
    private String oneTimePassword;

}
