package com.insurance.service;

import com.insurance.exception.InvalidPolicyDataException;
import com.insurance.exception.RecordNotFoundException;
import com.insurance.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** Keeps customers and policies in memory using HashMap and ArrayList. */
public class PolicyManager {
    private final Map<String, Customer> customers = new HashMap<>();
    private final Map<String, Policy> policiesById = new HashMap<>();
    private final List<Policy> policies = new ArrayList<>();
    private int customerCounter = 0;
    private int policyCounter = 0;

    public Customer addCustomer(String name, int age, Set<RiskFactor> risks)
            throws InvalidPolicyDataException {
        String id = String.format("C%03d", ++customerCounter);
        try {
            Customer customer = new Customer(id, name, age, risks);
            customers.put(id, customer);
            return customer;
        } catch (InvalidPolicyDataException e) {
            customerCounter--;   // do not waste an ID on invalid input
            throw e;
        }
    }

    public Customer getCustomer(String id) throws RecordNotFoundException {
        Customer customer = customers.get(id.toUpperCase());
        if (customer == null) {
            throw new RecordNotFoundException("No customer with ID " + id);
        }
        return customer;
    }

    public IndividualPolicy createIndividualPolicy(String customerId, CoverageTier tier)
            throws RecordNotFoundException, InvalidPolicyDataException {
        Customer holder = getCustomer(customerId);
        if (holder.getAge() < 18) {
            throw new InvalidPolicyDataException(
                    "Policyholder must be at least 18. Use a Family Floater for minors.");
        }
        IndividualPolicy policy = new IndividualPolicy(nextPolicyId(), tier, holder);
        store(policy);
        return policy;
    }

    public FamilyFloaterPolicy createFamilyFloater(List<String> memberIds, CoverageTier tier)
            throws RecordNotFoundException, InvalidPolicyDataException {
        if (memberIds.size() < FamilyFloaterPolicy.MIN_MEMBERS
                || memberIds.size() > FamilyFloaterPolicy.MAX_MEMBERS) {
            throw new InvalidPolicyDataException(String.format(
                    "A family floater needs %d to %d members.",
                    FamilyFloaterPolicy.MIN_MEMBERS, FamilyFloaterPolicy.MAX_MEMBERS));
        }
        List<Customer> members = new ArrayList<>();
        for (String id : memberIds) {
            Customer c = getCustomer(id);
            if (members.contains(c)) {
                throw new InvalidPolicyDataException("Duplicate member: " + id);
            }
            members.add(c);
        }
        boolean hasAdult = members.stream().anyMatch(c -> c.getAge() >= 18);
        if (!hasAdult) {
            throw new InvalidPolicyDataException("At least one member must be an adult.");
        }
        FamilyFloaterPolicy policy = new FamilyFloaterPolicy(nextPolicyId(), tier, members);
        store(policy);
        return policy;
    }

    public Policy getPolicy(String id) throws RecordNotFoundException {
        Policy policy = policiesById.get(id.toUpperCase());
        if (policy == null) {
            throw new RecordNotFoundException("No policy with ID " + id);
        }
        return policy;
    }

    public List<Customer> getAllCustomers() { return new ArrayList<>(customers.values()); }
    public List<Policy> getAllPolicies() { return new ArrayList<>(policies); }

    private String nextPolicyId() { return String.format("P%03d", ++policyCounter); }

    private void store(Policy policy) {
        policiesById.put(policy.getPolicyId(), policy);
        policies.add(policy);
    }
}
