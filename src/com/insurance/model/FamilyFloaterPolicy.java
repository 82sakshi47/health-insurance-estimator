package com.insurance.model;

import java.util.ArrayList;
import java.util.List;

/**
 * One shared sum insured for the whole family. Priced on the eldest member's
 * age band, the average medical risk, and a 30% add-on per extra member.
 */
public class FamilyFloaterPolicy extends Policy {
    public static final int MIN_MEMBERS = 2;
    public static final int MAX_MEMBERS = 6;
    private static final double PER_EXTRA_MEMBER = 0.30;

    private final List<Customer> members;

    public FamilyFloaterPolicy(String policyId, CoverageTier tier, List<Customer> members) {
        super(policyId, tier);
        this.members = new ArrayList<>(members);
    }

    @Override
    protected double premiumBeforeTax() {
        int eldest = members.stream().mapToInt(Customer::getAge).max().orElse(0);
        double avgLoading = members.stream()
                .mapToDouble(Customer::getRiskLoading).average().orElse(0);
        double familyFactor = 1 + PER_EXTRA_MEMBER * (members.size() - 1);
        return getTier().getBasePremium()
                * ageFactor(eldest)
                * (1 + avgLoading)
                * familyFactor;
    }

    @Override
    public String getPolicyType() { return "Family Floater"; }

    @Override
    public String describeHolders() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < members.size(); i++) {
            if (i > 0) sb.append(", ");
            sb.append(members.get(i).getName());
        }
        return sb.toString();
    }

    public List<Customer> getMembers() { return members; }
}
