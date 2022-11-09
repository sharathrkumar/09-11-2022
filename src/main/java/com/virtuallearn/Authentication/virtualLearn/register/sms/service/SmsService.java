package com.virtuallearn.Authentication.virtualLearn.register.sms.service;


import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import com.virtuallearn.Authentication.virtualLearn.register.sms.dao.DataAccessService;
import com.virtuallearn.Authentication.virtualLearn.register.sms.model.MobileAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SmsService {

    @Autowired
    DataAccessService dataAccessService;

    private final static String ACCOUNT_SID = "ACd7b80d5a6e82ec89f4be4cc8779fd230";
    private final static String AUTH_ID = "0d26480e6875454070f31b7860865873";

    static {
        Twilio.init(ACCOUNT_SID, AUTH_ID);
    }
    public long sendOtp(MobileAuth mobileAuth, String twoFaCode) {
        System.out.println(mobileAuth.getMobileNumber());
        Message.creator(new PhoneNumber(mobileAuth.getMobileNumber()), new PhoneNumber("+19896822968"),
                    "Your Two Factor Authentication code is: " + twoFaCode).create();
        return dataAccessService.saveOtp(mobileAuth.getMobileNumber(),twoFaCode);

    }

    public void deletePreviousOtp(String mobileNumber) {
        dataAccessService.deletePreviousOtp(mobileNumber);
    }

    public String verifyOtp(MobileAuth otp) {
        return dataAccessService.verifyOtp(otp);
    }

    public int checkMobileNumber(MobileAuth mobileAuth) {
        return dataAccessService.checkMobileNumber(mobileAuth.getMobileNumber());
    }

    public void resetPassword(MobileAuth auth) {
        dataAccessService.resetPassword(auth);
    }
}
