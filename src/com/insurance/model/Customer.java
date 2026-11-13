package com.insurance.model;

import com.insurance.exception.InvalidPolicyDataException;
import java.util.EnumSet;
import java.util.Set;

public class Customer {
    private final String id;
    private final String name;
    private final int age;
    private final Set<RiskFactor> riskFactors;

    public Customer(String id, String name, int age, Set<RiskFactor> riskFactors)
            throws InvalidPolicyDataException {
        if (name == null || name.isBlank()) {
            throw new InvalidPolicyDataException("Name cannot be empty.");
        }
        if (age < 0 || age > 100) {
            throw new InvalidPolicyDataException("Age must be between 0 and 100.");
        }
        this.id = id;
        this.name = name.trim();
        this.age = age;
        this.riskFactors = riskFactors.isEmpty()
                ? EnumSet.noneOf(RiskFactor.class)
                : EnumSet.copyOf(riskFactors);
    }

    /** Total risk loading for this person, capped at 100%. */
    public double getRiskLoading() {
        double total = 0;
        for (RiskFactor factor : riskFactors) {
            total += factor.getLoading();
        }
        return Math.min(total, 1.0);
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public Set<RiskFactor> getRiskFactors() { return riskFactors; }

    @Override
    public String toString() {
        return String.format("%s | %s | age %d | risks: %s", id, name, age,
                riskFactors.isEmpty() ? "none" : riskFactors);
    }
}
