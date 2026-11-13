package com.insurance.model;

/** Medical risk factors. The loading is the % added to the base premium. */
public enum RiskFactor {
    SMOKER(0.25),
    DIABETES(0.20),
    HYPERTENSION(0.15),
    OBESITY(0.10),
    HEART_DISEASE(0.40);

    private final double loading;

    RiskFactor(double loading) {
        this.loading = loading;
    }

    public double getLoading() { return loading; }
}
