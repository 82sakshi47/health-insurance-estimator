package com.insurance.model;

/** Anything that can produce a premium quote. */
public interface Premiumable {
    double calculatePremium();   // annual premium including GST
    String getPolicyType();
}
