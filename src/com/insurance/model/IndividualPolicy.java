package com.insurance.model;

public class IndividualPolicy extends Policy {
    private final Customer holder;

    public IndividualPolicy(String policyId, CoverageTier tier, Customer holder) {
        super(policyId, tier);
        this.holder = holder;
    }

    @Override
    protected double premiumBeforeTax() {
        return getTier().getBasePremium()
                * ageFactor(holder.getAge())
                * (1 + holder.getRiskLoading());
    }

    @Override
    public String getPolicyType() { return "Individual"; }

    @Override
    public String describeHolders() { return holder.getName(); }

    public Customer getHolder() { return holder; }
}
