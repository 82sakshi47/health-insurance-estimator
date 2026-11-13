package com.insurance.model;

public enum CoverageTier {
    BASIC(300_000, 5_000),
    STANDARD(500_000, 9_000),
    PREMIUM(1_000_000, 15_000);

    private final double sumInsured;
    private final double basePremium;

    CoverageTier(double sumInsured, double basePremium) {
        this.sumInsured = sumInsured;
        this.basePremium = basePremium;
    }

    public double getSumInsured() { return sumInsured; }
    public double getBasePremium() { return basePremium; }
}
