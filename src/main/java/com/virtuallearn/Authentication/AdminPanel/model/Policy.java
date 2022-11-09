package com.virtuallearn.Authentication.AdminPanel.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Policy {
    private Integer policyId;
    private String termsAndConditions;
    private String privacyPolicy;

    public Policy(String termsAndConditions, String privacyPolicy) {
        this.termsAndConditions = termsAndConditions;
        this.privacyPolicy = privacyPolicy;
    }
}
