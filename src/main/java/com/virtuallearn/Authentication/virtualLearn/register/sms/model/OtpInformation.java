package com.virtuallearn.Authentication.virtualLearn.register.sms.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OtpInformation {
    private String generatedTime;
    private String expiryTime;
    private long ValidUntil;
}
