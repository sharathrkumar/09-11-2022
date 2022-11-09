package com.virtuallearn.Authentication.virtualLearn.register.sms.controller;


import com.virtuallearn.Authentication.virtualLearn.register.sms.model.MobileAuth;
import com.virtuallearn.Authentication.virtualLearn.register.sms.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Random;

@RestController
@RequestMapping("/User")
public class TwoFactorServiceController {


    @Autowired
    SmsService service;


    @PutMapping("/Continue")
    public String sendCodeInSMS(@RequestBody MobileAuth mobileAuth) {
        int status = service.checkMobileNumber(mobileAuth);
        if (status == 1)
            return "User already exists";
        String twoFaCode = String.valueOf(new Random().nextInt(8999) + 1000);
        return "OTP Valid Until = " + service.sendOtp(mobileAuth, twoFaCode) + " Minutes";

    }


    @PutMapping("/Resend")
    public void ResendCodeInSMS(@RequestBody MobileAuth mobileAuth) {
        service.deletePreviousOtp(mobileAuth.getMobileNumber());
        String twoFaCode = String.valueOf(new Random().nextInt(8999) + 1000);
        service.sendOtp(mobileAuth, twoFaCode);
    }

    @PostMapping("/Verify")
    public String verifyOtp(@RequestBody MobileAuth otp) {
        return service.verifyOtp(otp);
    }

    @PostMapping("/Send")
    public ResponseEntity<?> sendOtp(@RequestBody MobileAuth auth) {
        int status = service.checkMobileNumber(auth);
        if (status == 0)
            return new ResponseEntity<String>("User not registered", HttpStatus.NOT_FOUND);
        String twoFaCode = String.valueOf(new Random().nextInt(8999) + 1000);
        service.sendOtp(auth, twoFaCode);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/ResetPassword")
    public ResponseEntity<?> resetPassword(@RequestBody MobileAuth auth) {
        service.resetPassword(auth);
        return ResponseEntity.ok().build();
    }

//    @PutMapping("/submit/{OTP}")
//    public ResponseEntity<Object> validateOTP(@PathVariable String OTP){
//        if(OTP.equals(this.OTP))
//            return ResponseEntity.ok().build();
//        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
//
//    }

}
