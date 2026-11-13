package com.insurance.model;

/** Base class for all policies. Subclasses supply the pricing details. */
public abstract class Policy implements Premiumable {
    protected static final double GST = 0.18;

    private final String policyId;
    private final CoverageTier tier;

    protected Policy(String policyId, CoverageTier tier) {
        this.policyId = policyId;
        this.tier = tier;
    }

    /** Age band multiplier: older people cost more to insure. */
    protected static double ageFactor(int age) {
        if (age < 18) return 0.8;
        if (age <= 30) return 1.0;
        if (age <= 45) return 1.3;
        if (age <= 60) return 1.8;
        return 2.5;
    }

    /** Premium before GST. Each policy type implements its own rules. */
    protected abstract double premiumBeforeTax();

    @Override
    public double calculatePremium() {
        return premiumBeforeTax() * (1 + GST);
    }

    public abstract String describeHolders();

    public String getPolicyId() { return policyId; }
    public CoverageTier getTier() { return tier; }

    @Override
    public String toString() {
        return String.format("%s | %-14s | %-8s | cover Rs.%,.0f | holders: %s | premium Rs.%,.2f/yr",
                policyId, getPolicyType(), tier, tier.getSumInsured(),
                describeHolders(), calculatePremium());
    }
}
